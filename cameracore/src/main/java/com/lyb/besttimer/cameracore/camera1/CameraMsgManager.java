package com.lyb.besttimer.cameracore.camera1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.lyb.besttimer.cameracore.AngleUtil;
import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.CameraState;
import com.lyb.besttimer.commonutil.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraMsgManager {

    private static final String TAG = "CameraMsgManager";
    private final Activity activity;
    private final SurfaceView surfaceView;
    private CameraResultCaller cameraResultCaller;
    private Camera mCamera;
    private int mCameraId;
    private int mFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
    private SurfaceHolder surfaceHolder;
    private int sensorRotation;
    private MediaRecorder mediaRecorder;

    private CameraState cameraState = CameraState.PREVIEW;

    public CameraMsgManager(Activity activity, SurfaceView surfaceView) {
        this.activity = activity;
        this.surfaceView = surfaceView;
    }

    public void setCameraResultCaller(CameraResultCaller cameraResultCaller) {
        this.cameraResultCaller = cameraResultCaller;
    }

    /**
     * 是否使用控制方向陀螺感应器
     */
    private boolean useSensor = true;

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                return;
            }
            float[] values = event.values;
            sensorRotation = AngleUtil.getSensorRotation(values[SensorManager.DATA_X], values[SensorManager.DATA_Y]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

        }
    };

    public void onCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        switchCamera(mFacing);
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
        int previewWidth;
        int previewHeight;
        if (changeSizeOrientation()) {
            previewWidth = size.height;
            previewHeight = size.width;
        } else {
            previewWidth = size.width;
            previewHeight = size.height;
        }
        if (surfaceView.getWidth() * previewHeight > previewWidth * surfaceView.getHeight()) {
            layoutParams.height = (int) (surfaceView.getWidth() * previewHeight / previewWidth);
        } else {
            layoutParams.width = (int) (surfaceView.getHeight() * previewWidth / previewHeight);
        }
        surfaceView.setLayoutParams(layoutParams);
    }

    public void onStarted() {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.lock();
            resumePreview();
            cameraState = CameraState.PREVIEW;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onDestroyed() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            try {
                mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.release();
            mCamera = null;
        }
    }

    public void switchCamera() {
        if (cameraState == CameraState.PREVIEW) {
            if (mFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
        }
    }

    public void switchCamera(int facing) {
        if (mCamera == null || facing != mFacing) {
            onDestroyed();

            boolean toStartPreview = facing != mFacing;
            mFacing = facing;

            Camera.CameraInfo info = new Camera.CameraInfo();
            int numCameras = Camera.getNumberOfCameras();

            for (int i = 0; i < numCameras; ++i) {
                Camera.getCameraInfo(i, info);
                if (info.facing == facing) {
                    mCamera = Camera.open(i);
                    mCameraId = i;
                    break;
                }
            }

            if (mCamera == null) {
                mCamera = Camera.open();
                mCameraId = 0;
            }

            Camera.Parameters parameters = mCamera.getParameters();
            chooseFixedPreviewFps(parameters, 10);
            mCamera.setDisplayOrientation(calculateCameraPreviewOrientation(activity));

//            Camera.Size size_picture;
//            if (changeSizeOrientation()) {
//                size_picture = calculatePerfectSize(parameters.getSupportedPictureSizes(), surfaceView.getHeight(), surfaceView.getWidth());
//            } else {
//                size_picture = calculatePerfectSize(parameters.getSupportedPictureSizes(), surfaceView.getWidth(), surfaceView.getHeight());
//            }

//            Camera.Size size_preview;
//            if (changeSizeOrientation()) {
//                size_preview = calculatePerfectSize(parameters.getSupportedPreviewSizes(), size_picture.height, size_picture.width);
//            } else {
//                size_preview = calculatePerfectSize(parameters.getSupportedPreviewSizes(), size_picture.width, size_picture.height);
//            }

            Pair<Camera.Size, Camera.Size> size_pre_pic;
            if (changeSizeOrientation()) {
                size_pre_pic = calculatePerfectSize(parameters.getSupportedPreviewSizes(), parameters.getSupportedPictureSizes(), surfaceView.getHeight(), surfaceView.getWidth());
            } else {
                size_pre_pic = calculatePerfectSize(parameters.getSupportedPreviewSizes(), parameters.getSupportedPictureSizes(), surfaceView.getWidth(), surfaceView.getHeight());
            }
            Camera.Size size_preview = size_pre_pic.first;
            Camera.Size size_picture = size_pre_pic.second;

//            Camera.Size size_picture;
//            if (changeSizeOrientation()) {
//                size_picture = calculatePerfectSize(parameters.getSupportedPictureSizes(), size_preview.height, size_preview.width);
//            } else {
//                size_picture = calculatePerfectSize(parameters.getSupportedPictureSizes(), size_preview.width, size_preview.height);
//            }

            parameters.setPreviewSize(size_preview.width, size_preview.height);
            parameters.setPictureSize(size_picture.width, size_picture.height);
            if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if (parameters.getSupportedPictureFormats().contains(ImageFormat.JPEG)) {
                parameters.setPictureFormat(ImageFormat.JPEG);
                parameters.setJpegQuality(100);
            }
            mCamera.setParameters(parameters);

            if (cameraResultCaller != null) {
                cameraResultCaller.onCameraReady();
            }

            if (toStartPreview) {
                onStarted();
            }

        }
    }

    public void resumePreview() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            parameters.setRecordingHint(false);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    public void pausePreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    /**
     * 是否使用控制方向陀螺感应器
     */
    public void controlSensor(boolean enabled) {
        this.useSensor = enabled;
        if (enabled) {
            unregisterSensorManagerInner();
            registerSensorManagerInner();
        } else {
            unregisterSensorManagerInner();
            sensorRotation = 0;
        }
    }

    public void registerSensorManager() {
        if (useSensor) {
            registerSensorManagerInner();
        }
    }

    public void registerSensorManagerInner() {
        SensorManager sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager
                .SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorManager() {
        if (useSensor) {
            unregisterSensorManagerInner();
        }
    }

    public void unregisterSensorManagerInner() {
        SensorManager sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(sensorEventListener);
    }

    static class CompareSizesByArea implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.width * lhs.height -
                    (long) rhs.width * rhs.height);
        }

    }

    private int calculateCameraPreviewOrientation(Activity activity) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
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
            default:
        }

        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    private boolean changeSizeOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
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
            default:
        }
        return (degrees + info.orientation) % 180 != 0;
    }

    private Camera.Size calculatePerfectSize(List<Camera.Size> sizes, int expectWidth, int expectHeight) {
        Camera.Size result = null;
        double targetRatio = expectWidth * 1.0 / expectHeight;
        for (Camera.Size size : sizes) {
            if (result == null) {
                result = size;
            } else {
                double resultRatio = result.width * 1.0 / result.height;
                double currRatio = size.width * 1.0 / size.height;
                if (Math.abs(resultRatio - targetRatio) > Math.abs(currRatio - targetRatio)) {
                    result = size;
                } else if (Math.abs(resultRatio - targetRatio) == Math.abs(currRatio - targetRatio)) {
                    if (size.width * size.height > result.width * result.height) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }

    private Camera.Size choosePictureSize(List<Camera.Size> sizes_picture, int expectWidth, int expectHeight) {
        return calculatePerfectSize(sizes_picture, null, null, null, expectWidth, expectHeight);
    }

    private Pair<Camera.Size, Camera.Size> calculatePerfectSize(List<Camera.Size> sizes_preview, List<Camera.Size> sizes_picture, int expectWidth, int expectHeight) {

        Camera.Size pictureSize = choosePictureSize(sizes_picture, expectWidth, expectHeight);

        Camera.Size previewSize = calculatePerfectSize(sizes_preview, pictureSize, 1920, 1080, expectWidth, expectHeight);

        return new Pair<>(previewSize, pictureSize);

    }

    private Camera.Size calculatePerfectSize(List<Camera.Size> sizes, @Nullable Camera.Size standardSize, Integer widthLimit, Integer heightLimit, int expectWidth, int expectHeight) {

        Collections.sort(sizes, new CompareSizesByArea());

        List<Camera.Size> bigEnough = new ArrayList<>();
        List<Camera.Size> notBigEnough = new ArrayList<>();
        for (Camera.Size option : sizes) {
            boolean widthPass = widthLimit == null || option.width <= widthLimit;
            boolean heightPass = heightLimit == null || option.height <= heightLimit;
            boolean sizePass = standardSize == null || option.height * standardSize.width == option.width * standardSize.height;
            if (widthPass && heightPass && sizePass) {
                if (option.width >= expectWidth &&
                        option.height >= expectHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        Camera.Size resultSize;
        if (bigEnough.size() > 0) {
            resultSize = Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            resultSize = Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable size");
            resultSize = sizes.get(sizes.size() - 1);
        }

        return resultSize;

    }

    private void chooseFixedPreviewFps(Camera.Parameters parameters, int expectedThoudandFps) {

        List<int[]> supportedFps = parameters.getSupportedPreviewFpsRange();

        for (int[] range : supportedFps) {
            if (range[0] <= expectedThoudandFps && range[1] >= expectedThoudandFps) {
                parameters.setPreviewFpsRange(range[0], range[1]);
                break;
            }
        }

    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    private Rect calculateTapArea(float x, float y, float coefficient) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / surfaceView.getWidth() * 2000 - 1000);
        int centerY = (int) (y / surfaceView.getHeight() * 2000 - 1000);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int right = clamp(left + areaSize, -1000, 1000);
        int bottom = clamp(top + areaSize, -1000, 1000);
        RectF rectF = new RectF(left, top, right, bottom);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF
                .bottom));
    }

    public void clickShow(float x, float y) {
        final Camera.Parameters params = mCamera.getParameters();
        Rect clickRect = calculateTapArea(x, y, 1f);
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(clickRect, 800));
            params.setFocusAreas(focusAreas);
        }
        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(clickRect, 800));
            params.setMeteringAreas(meteringAreas);
        }
        String currentFocusMode = params.getFocusMode();
        if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.cancelAutoFocus();
        mCamera.setParameters(params);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Camera.Parameters params = camera.getParameters();
                params.setFocusMode(currentFocusMode);
                camera.setParameters(params);
            }
        });
    }

    private int initZoom = 0;

    public void initZoom() {
        if (mCamera != null) {
            Camera.Parameters parameter = mCamera.getParameters();
            initZoom = parameter.getZoom();
        }
    }

    public void offsetZoomByMode(int offsetZoom) {
        if (cameraState == CameraState.PREVIEW) {
            offsetZoom(offsetZoom);
        }
    }

    public void offsetZoom(int offsetZoom) {
        if (mCamera != null) {
            try {
                Camera.Parameters parameter = mCamera.getParameters();
//            if (parameter.isSmoothZoomSupported()) {
                if (parameter.isZoomSupported()) {
                    int currZoom = initZoom + offsetZoom;
                    currZoom = Math.min(Math.max(currZoom, 0), parameter.getMaxZoom());
                    parameter.setZoom(currZoom);
                    mCamera.setParameters(parameter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 控制flash模式
     *
     * @param value 参考
     *              {@link Camera.Parameters#FLASH_MODE_AUTO}
     *              {@link Camera.Parameters#FLASH_MODE_OFF}
     *              {@link Camera.Parameters#FLASH_MODE_ON}
     *              {@link Camera.Parameters#FLASH_MODE_RED_EYE}
     *              {@link Camera.Parameters#FLASH_MODE_TORCH}
     */
    public void controlFlashMode(String value) {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(value);
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 控制flash模式
     *
     * @return 参考
     * {@link Camera.Parameters#FLASH_MODE_AUTO}
     * {@link Camera.Parameters#FLASH_MODE_OFF}
     * {@link Camera.Parameters#FLASH_MODE_ON}
     * {@link Camera.Parameters#FLASH_MODE_RED_EYE}
     * {@link Camera.Parameters#FLASH_MODE_TORCH}
     */
    public String getFlashMode() {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                return parameters.getFlashMode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void takePicture() {
        if (mCamera != null) {
            if (cameraState == CameraState.PREVIEW) {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        onStarted();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        int rotationValue = (sensorRotation - activity.getWindowManager().getDefaultDisplay().getRotation() + 4) % 4;

                        float viewWidth = ((ViewGroup) surfaceView.getParent()).getWidth();
                        float viewHeight = ((ViewGroup) surfaceView.getParent()).getHeight();

                        Matrix matrix = new Matrix();
                        Camera.CameraInfo info = new Camera.CameraInfo();
                        Camera.getCameraInfo(mCameraId, info);
                        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            int degrees = (360 - rotationValue * 90 + calculateCameraPreviewOrientation(activity)) % 360;
                            matrix.setRotate(degrees);
                            if (degrees % 180 == 0) {
                                float temp = viewWidth;
                                viewWidth = viewHeight;
                                viewHeight = temp;
                            }
                        } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            int degrees = (360 + rotationValue * 90 + calculateCameraPreviewOrientation(activity) + 180) % 360;
                            matrix.setRotate(degrees);
                            if (degrees % 180 == 0) {
                                float temp = viewWidth;
                                viewWidth = viewHeight;
                                viewHeight = temp;
                            }
                        }
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                        float bitmapWidth = bitmap.getWidth();
                        float bitmapHeight = bitmap.getHeight();
                        float widthRatio = viewWidth / bitmapWidth;
                        float heightRatio = viewHeight / bitmapHeight;
                        if (widthRatio > heightRatio) {
                            heightRatio = heightRatio / widthRatio;
                            widthRatio = 1;
                        } else {
                            widthRatio = widthRatio / heightRatio;
                            heightRatio = 1;
                        }
                        bitmap = Bitmap.createBitmap(bitmap,
                                (int) ((1 - widthRatio) / 2 * bitmapWidth),
                                (int) ((1 - heightRatio) / 2 * bitmapHeight),
                                (int) (widthRatio * bitmapWidth),
                                (int) (heightRatio * bitmapHeight));
                        String path = FileUtil.saveBitmap(activity, "htjyCamera", bitmap);
                        // 最后通知图库更新
                        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

                        if (cameraResultCaller != null) {
                            cameraResultCaller.onResult(path, CameraResultCaller.ResultType.PICTURE);
                        }

                    }
                });
            }
        }
    }

    public void takeRecord() {
        if (mCamera != null) {
            if (cameraState == CameraState.PREVIEW) {
                startRecord();
            } else if (cameraState == CameraState.VIDEO) {
                stopRecord();
            }
        }
    }

    private String videoPath;

    private void startRecord() {

        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        parameters.setRecordingHint(true);
        mCamera.setParameters(parameters);
        mCamera.unlock();

        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        Camera.Size size_video;
        if (parameters.getSupportedVideoSizes() == null) {
            size_video = parameters.getPreviewSize();
        } else {
            if (changeSizeOrientation()) {
                size_video = calculatePerfectSize(parameters.getSupportedVideoSizes(), ((ViewGroup) surfaceView.getParent()).getHeight(), ((ViewGroup) surfaceView.getParent()).getWidth());
            } else {
                size_video = calculatePerfectSize(parameters.getSupportedVideoSizes(), ((ViewGroup) surfaceView.getParent()).getWidth(), ((ViewGroup) surfaceView.getParent()).getHeight());
            }
        }

        mediaRecorder.setVideoSize(size_video.width, size_video.height);

//        mediaRecorder.setVideoFrameRate(20);

        int rotationValue = (sensorRotation - activity.getWindowManager().getDefaultDisplay().getRotation() + 4) % 4;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mediaRecorder.setOrientationHint((360 - rotationValue * 90 + calculateCameraPreviewOrientation(activity)) % 360);
        } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mediaRecorder.setOrientationHint((360 + rotationValue * 90 + calculateCameraPreviewOrientation(activity) + 180) % 360);
//            matrix.postScale(-1, 1);
        }

        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);

        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

        String videoFileName = "video_" + System.currentTimeMillis() + ".mp4";
        File parentFile = FileUtil.getDir(activity, "BesttimerVideo");
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        mediaRecorder.setOutputFile(videoPath = new File(parentFile, videoFileName).getPath());

        try {
            cameraState = CameraState.VIDEO;
            mediaRecorder.prepare();
            mediaRecorder.start();
            if (cameraResultCaller != null) {
                cameraResultCaller.onStartVideo();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopRecord() {

        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (Exception e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                e.printStackTrace();
                mediaRecorder = null;
                videoPath = null;
                mediaRecorder = new MediaRecorder();
            } finally {
                if (mediaRecorder != null) {
                    mediaRecorder.release();
                }
                mediaRecorder = null;
                onStarted();

                if (!TextUtils.isEmpty(videoPath)) {

                    // 最后通知图库更新
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + videoPath)));

                    if (cameraResultCaller != null) {
                        cameraResultCaller.onResult(videoPath, CameraResultCaller.ResultType.VIDEO);
                    }

                }

            }
        }

    }

}
