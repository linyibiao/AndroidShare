package com.lyb.besttimer.cameracore.camera2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
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
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
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
import com.lyb.besttimer.cameracore.CameraState;
import com.lyb.besttimer.cameracore.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraMsgManager {

    private final Activity activity;
    private final TextureView textureView;
    private Surface surface;
    private String mCameraId;
    private int mFacing = CameraCharacteristics.LENS_FACING_BACK;
    private CameraDevice mCameraDevice;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private ImageReader mImageReader;
    private MediaRecorder mMediaRecorder;
    private Size size_preview;
    private Size size_video;

    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest mPreviewRequest;
    private int sensorRotation;

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private CameraState cameraState = CameraState.PREVIEW;

    public CameraMsgManager(Activity activity, TextureView textureView) {
        this.activity = activity;
        this.textureView = textureView;
//        this.surface=new Surface(textureView.getSurfaceTexture());
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
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
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
        closePreviewSession();
        createSession(cameraState = CameraState.PREVIEW, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                try {

                    CaptureRequest.Builder newBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    copyCapture(newBuilder, mCaptureRequestBuilder);
                    mCaptureRequestBuilder = newBuilder;
                    mCaptureRequestBuilder.addTarget(surface);

                    mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    CaptureRequest mPreviewRequest = mCaptureRequestBuilder.build();
                    mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {

            }
        });
    }

    private void copyCapture(CaptureRequest.Builder newCaptureRequestBuilder, CaptureRequest.Builder oldCaptureRequestBuilder) {
        if (newCaptureRequestBuilder != null && oldCaptureRequestBuilder != null) {
            newCaptureRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, oldCaptureRequestBuilder.get(CaptureRequest.SCALER_CROP_REGION));
        }
    }

    private void createSession(CameraState cameraState, CameraCaptureSession.StateCallback stateCallback) {
        if (mCameraDevice == null) {
            return;
        }
        try {
            closePreviewSession();
            List<Surface> outputs = new ArrayList<>();
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(size_preview.getWidth(), size_preview.getHeight());
            surface = new Surface(texture);
            if (cameraState == CameraState.PREVIEW) {
                outputs.add(surface);
            } else if (cameraState == CameraState.PHOTO) {
                outputs.add(surface);
                outputs.add(mImageReader.getSurface());
            } else if (cameraState == CameraState.VIDEO) {
                outputs.add(surface);
                outputs.add(mMediaRecorder.getSurface());
            }
            mCameraDevice.createCaptureSession(outputs,
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }
                            mCaptureSession = session;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stateCallback.onConfigured(session);
                                }
                            });
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            stateCallback.onConfigureFailed(session);
                        }
                    }, mBackgroundHandler
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

                size_video = calculatePerfectSize(map.getOutputSizes(MediaRecorder.class), size_preview.getWidth(), size_preview.getHeight());

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

                SurfaceTexture texture = textureView.getSurfaceTexture();
                texture.setDefaultBufferSize(size_preview.getWidth(), size_preview.getHeight());
                surface = new Surface(texture);

                // For still image captures, we use the largest available size.
//                Size largest = calculatePerfectSize(map.getOutputSizes(ImageFormat.JPEG), size_preview.getWidth(), size_preview.getHeight());
                mImageReader = ImageReader.newInstance(size_picture.getWidth(), size_picture.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);

                mMediaRecorder = new MediaRecorder();

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

    private void closePreviewSession() {
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }

    private void captureStillPicture() {
        if (mCaptureSession != null) {
            createSession(cameraState = CameraState.PHOTO, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                        copyCapture(captureBuilder, mCaptureRequestBuilder);
                        mCaptureRequestBuilder = captureBuilder;

                        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        mCaptureRequestBuilder.addTarget(mImageReader.getSurface());

                        int rotationValue = (sensorRotation - activity.getWindowManager().getDefaultDisplay().getRotation() + 4) % 4;
                        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
                        CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
                        Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                        if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                            mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, (360 - rotationValue * 90 + calculateCameraPreviewOrientation(activity)) % 360);
                        } else if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                            mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, (360 + rotationValue * 90 + calculateCameraPreviewOrientation(activity) + 180) % 360);
//                        matrix.postScale(-1, 1);
                        }

                        CameraCaptureSession.CaptureCallback CaptureCallback
                                = new CameraCaptureSession.CaptureCallback() {

                            @Override
                            public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                           @NonNull CaptureRequest request,
                                                           @NonNull TotalCaptureResult result) {
                                createCameraPreviewSession();
//                    showToast("Saved: " + mFile);
//                    Log.d(TAG, mFile.toString());
//                                unlockFocus();
                            }
                        };

                        mCaptureSession.stopRepeating();
                        mCaptureSession.abortCaptures();
                        mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            });
        }
    }

    private void setUpMediaRecorder() throws IOException {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        String videoFileName = "video_" + System.currentTimeMillis() + ".mp4";
        File parentFile = FileUtil.getDir(activity, "BesttimerVideo2");
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        mMediaRecorder.setOutputFile(videoPath = new File(parentFile, videoFileName).getPath());

        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(size_video.getWidth(), size_video.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            int rotationValue = (sensorRotation - activity.getWindowManager().getDefaultDisplay().getRotation() + 4) % 4;
            CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                mMediaRecorder.setOrientationHint((360 - rotationValue * 90 + calculateCameraPreviewOrientation(activity)) % 360);
            } else if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                mMediaRecorder.setOrientationHint((360 + rotationValue * 90 + calculateCameraPreviewOrientation(activity) + 180) % 360);
//            matrix.postScale(-1, 1);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        mMediaRecorder.prepare();
    }

    public void clickShow(float x, float y) {
        if (mCaptureSession != null) {
            try {
                CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
                Rect sensor = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                int focusX;
                int focusY;
                if (changeSizeOrientation()) {
                    focusX = (int) (x / textureView.getWidth() * sensor.height());
                    focusY = (int) (y / textureView.getHeight() * sensor.width());
                } else {
                    focusX = (int) (x / textureView.getWidth() * sensor.width());
                    focusY = (int) (y / textureView.getHeight() * sensor.height());
                }
                int radius = 10;
                Rect focusRect = new Rect(focusX - radius, focusY - radius, focusX + radius, focusY + radius);

                MeteringRectangle[] rectangle = new MeteringRectangle[]{new MeteringRectangle(focusRect, 1000)};
                // 对焦模式必须设置为AUTO
                mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
                //AE
                mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, rectangle);
                //AF 此处AF和AE用的同一个rect, 实际AE矩形面积比AF稍大, 这样测光效果更好
                mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, rectangle);
                try {
                    // AE/AF区域设置通过setRepeatingRequest不断发请求
                    mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
                    mCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                //触发对焦
                mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
                try {
                    //触发对焦通过capture发送请求, 因为用户点击屏幕后只需触发一次对焦
                    mCaptureSession.capture(mCaptureRequestBuilder.build(), null, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Rect cropRegionForZoom(CameraCharacteristics characteristics, float zoom) {
        Rect sensor = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        int xCenter = sensor.width() / 2;
        int yCenter = sensor.height() / 2;
        int xDelta = (int) (0.5f * sensor.width() / zoom);
        int yDelta = (int) (0.5f * sensor.height() / zoom);
        return new Rect(xCenter - xDelta, yCenter - yDelta, xCenter + xDelta, yCenter + yDelta);
    }

    private float initZoom = 1f;

    public void initZoom() {
        if (mCaptureSession != null) {
            try {
                CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
                Rect sensor = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                Rect currRect = mCaptureRequestBuilder.get(CaptureRequest.SCALER_CROP_REGION);
                initZoom = 1;
                if (currRect != null) {
                    initZoom = sensor.width() * 1.0f / currRect.width();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void offsetZoom(float offsetZoom) {
        if (mCaptureSession != null) {
            try {
                CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
                Float maxZoom = characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
                if (maxZoom != null && maxZoom > 1) {
                    float currZoom = initZoom + offsetZoom;
                    currZoom = Math.min(Math.max(currZoom, 1f), maxZoom);
                    mCaptureRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, cropRegionForZoom(characteristics, currZoom));
                    mPreviewRequest = mCaptureRequestBuilder.build();
                    mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拍照
     */
    public void takePicture() {
        if (mCaptureSession != null) {
            if (cameraState == CameraState.PREVIEW) {
                captureStillPicture();
            }
        }
    }

    private String videoPath;

    /**
     * 视频
     */
    public void takeRecord() {
        if (mCaptureSession != null) {
            if (cameraState == CameraState.VIDEO) {//正在录制视频
                stopRecord();
            } else if (cameraState == CameraState.PREVIEW) {//还没开始录制视频
                startRecord();
            }
        }
    }

    private void startRecord() {
        try {
            setUpMediaRecorder();
            createSession(cameraState = CameraState.VIDEO, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        CaptureRequest.Builder newBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                        copyCapture(newBuilder, mCaptureRequestBuilder);
                        mCaptureRequestBuilder = newBuilder;

                        mCaptureRequestBuilder.addTarget(surface);
                        Surface recorderSurface = mMediaRecorder.getSurface();
                        mCaptureRequestBuilder.addTarget(recorderSurface);

                        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);

                        mPreviewRequest = mCaptureRequestBuilder.build();
                        mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);

                        mMediaRecorder.start();

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {

        closePreviewSession();

        mMediaRecorder.stop();
        mMediaRecorder.reset();

        // 最后通知图库更新
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + videoPath)));

        createCameraPreviewSession();

    }

}
