package com.lyb.besttimer.cameracore.camera2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lyb.besttimer.cameracore.AngleUtil;
import com.lyb.besttimer.cameracore.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraMsgManager {

    private final Activity activity;
    private final TextureView textureView;
    private String mCameraId;
    private int mFacing = CameraCharacteristics.LENS_FACING_BACK;
    private CameraDevice mCameraDevice;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private ImageReader mImageReader;
    private Size size_preview;

    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest mPreviewRequest;
    private int sensorRotation;

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    public CameraMsgManager(Activity activity, TextureView textureView) {
        this.activity = activity;
        this.textureView = textureView;
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                return;
            }
            float[] values = event.values;
            sensorRotation = AngleUtil.getSensorRotation(values[SensorManager.DATA_X], values[SensorManager.DATA_Y]);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void switchCamera() {
        if (mFacing == CameraCharacteristics.LENS_FACING_BACK) {
            mFacing = CameraCharacteristics.LENS_FACING_FRONT;
        } else {
            mFacing = CameraCharacteristics.LENS_FACING_BACK;
        }
        onPause();
        onResume();
    }

    public void onResume() {
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    public void onPause() {
        closeCamera();
        stopBackgroundThread();
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quit();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        setUpCameraOutputs();
        configureTransform();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform();
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
        }

    };

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            activity.finish();
        }

    };

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            File parentFile = FileUtil.getDir(activity, "BesttimerCamera2");
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            String picFileName = "picture_" + System.currentTimeMillis() + ".jpg";
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), new File(parentFile, picFileName)));
        }

    };

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(size_preview.getWidth(), size_preview.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
//                                setAutoFlash(mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
//                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCameraOutputs() {
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (mFacing != facing) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                mCameraId = cameraId;

                Pair<Size, Size> size_pre_pic;
                if (changeSizeOrientation()) {
                    size_pre_pic = calculatePerfectSize(map.getOutputSizes(SurfaceTexture.class), map.getOutputSizes(ImageFormat.JPEG), textureView.getHeight(), textureView.getWidth());
                } else {
                    size_pre_pic = calculatePerfectSize(map.getOutputSizes(SurfaceTexture.class), map.getOutputSizes(ImageFormat.JPEG), textureView.getWidth(), textureView.getHeight());
                }
                size_preview = size_pre_pic.first;
                Size size_picture = size_pre_pic.second;

                ViewGroup.LayoutParams layoutParams = textureView.getLayoutParams();
                if (changeSizeOrientation()) {
                    layoutParams.height = (int) (textureView.getWidth() * 1.0 * size_preview.getWidth() / size_preview.getHeight());
                } else {
                    layoutParams.height = (int) (textureView.getWidth() * 1.0 * size_preview.getHeight() / size_preview.getWidth());
                }
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    ((LinearLayout.LayoutParams) layoutParams).weight = 0;
                }
                textureView.setLayoutParams(layoutParams);

                // For still image captures, we use the largest available size.
//                Size largest = calculatePerfectSize(map.getOutputSizes(ImageFormat.JPEG), size_preview.getWidth(), size_preview.getHeight());
                mImageReader = ImageReader.newInstance(size_picture.getWidth(), size_picture.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);

                // Check if the flash is supported.
//                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
//                mFlashSupported = available == null ? false : available;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void configureTransform() {
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, textureView.getWidth(), textureView.getHeight());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        int rotation = (calculateCameraPreviewOrientation(activity) + (360 - 90)) % 360;
        if (rotation % 180 != 0) {
            RectF bufferRect = new RectF(0, 0, size_preview.getHeight(), size_preview.getWidth());
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    viewRect.height() / size_preview.getHeight(),
                    viewRect.width() / size_preview.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
        }
        matrix.postRotate(rotation, centerX, centerY);
        textureView.setTransform(matrix);
    }

    private Size calculatePerfectSize(Size[] sizes, int expectWidth, int expectHeight) {
        return calculatePerfectSize(sizes, expectWidth, expectHeight, -1, -1);
    }

    private Size calculatePerfectSize(Size[] sizes, int expectWidth, int expectHeight, int maxWidth, int maxHeight) {
        Size result = null;
        double targetRatio = expectWidth * 1.0 / expectHeight;
        for (Size size : sizes) {
            if (maxWidth > 0 && maxHeight > 0) {
                if (size.getWidth() > maxWidth || size.getHeight() > maxHeight) {
                    continue;
                }
            }
            if (result == null) {
                result = size;
            } else {
                double resultRatio = result.getWidth() * 1.0 / result.getHeight();
                double currRatio = size.getWidth() * 1.0 / size.getHeight();
                if (Math.abs(resultRatio - targetRatio) > Math.abs(currRatio - targetRatio)) {
                    result = size;
                } else if (Math.abs(resultRatio - targetRatio) == Math.abs(currRatio - targetRatio)) {
                    if (size.getWidth() * size.getHeight() > result.getWidth() * result.getHeight()) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }

    private Pair<Size, Size> calculatePerfectSize(Size[] sizes_preview, Size[] sizes_picture, int expectWidth, int expectHeight) {
        List<Pair<Size, Size>> availableSizes = new ArrayList<>();
        for (Size size_preview : sizes_preview) {
            Size pictureSize = null;
            for (Size size_picture : sizes_picture) {
                if (size_preview.getWidth() <= 1920 && size_preview.getHeight() <= 1080) {
                    if (size_preview.getWidth() * size_picture.getHeight() == size_preview.getHeight() * size_picture.getWidth()) {
                        if (pictureSize == null) {
                            pictureSize = size_picture;
                        } else if (size_picture.getWidth() * size_picture.getHeight() > pictureSize.getWidth() * pictureSize.getHeight()) {
                            pictureSize = size_picture;
                        }
                    }
                }
            }
            if (pictureSize != null) {
                availableSizes.add(new Pair<>(size_preview, pictureSize));
            }
        }
        Pair<Size, Size> result = null;
        double targetRatio = expectWidth * 1.0 / expectHeight;
        for (Pair<Size, Size> size : availableSizes) {
            if (result == null) {
                result = size;
            } else {
                double resultRatio = result.first.getWidth() * 1.0 / result.first.getHeight();
                double currRatio = size.first.getWidth() * 1.0 / size.first.getHeight();
                if (Math.abs(resultRatio - targetRatio) > Math.abs(currRatio - targetRatio)) {
                    result = size;
                } else if (Math.abs(resultRatio - targetRatio) == Math.abs(currRatio - targetRatio)) {
                    if (size.first.getWidth() * size.first.getHeight() > result.first.getWidth() * result.first.getHeight()) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }

    private int calculateCameraPreviewOrientation(Activity activity) {
        try {
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            Integer cameraOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
            }

            int result = 0;
            if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                result = (cameraOrientation + degrees) % 360;
                result = (360 - result) % 360;
            } else if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                result = (cameraOrientation - degrees + 360) % 360;
            }

            return result;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean changeSizeOrientation() {
        try {
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            Integer cameraOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
            }
            return (degrees + cameraOrientation) % 180 != 0;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 最后通知图库更新
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFile.getPath())));
            }
        }

    }

    public void registerSensorManager() {
        SensorManager sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager
                .SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorManager() {
        SensorManager sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(sensorEventListener);
    }

    /**
     * 拍照
     */
    public void takePicture() {
        if (mCaptureSession != null) {
            captureStillPicture();
        }
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus() {
        try {
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void captureStillPicture() {
        if (mCaptureSession != null) {
            try {
                CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureBuilder.addTarget(mImageReader.getSurface());

                // Use the same AE and AF modes as the preview.
                captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//            setAutoFlash(captureBuilder);

                // Orientation
                int rotationValue = (sensorRotation - activity.getWindowManager().getDefaultDisplay().getRotation() + 4) % 4;
                CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                    captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, (360 - rotationValue * 90 + calculateCameraPreviewOrientation(activity)) % 360);
                } else if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, (360 - rotationValue * 90 + calculateCameraPreviewOrientation(activity) + 180) % 360);
//                        matrix.postScale(-1, 1);
                }

                lockFocus();

                CameraCaptureSession.CaptureCallback CaptureCallback
                        = new CameraCaptureSession.CaptureCallback() {

                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                   @NonNull CaptureRequest request,
                                                   @NonNull TotalCaptureResult result) {
//                    showToast("Saved: " + mFile);
//                    Log.d(TAG, mFile.toString());
                        unlockFocus();
                    }
                };

                mCaptureSession.stopRepeating();
                mCaptureSession.abortCaptures();
                mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
