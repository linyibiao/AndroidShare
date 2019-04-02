package com.lyb.besttimer.camera1app;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

public class CameraMsgManager {

    private final Activity activity;
    private final SurfaceView surfaceView;
    private final Camera.PreviewCallback mPreviewCallback;
    private Camera mCamera;
    private int mCameraId;
    private int mFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
    private SurfaceHolder surfaceHolder;

    public CameraMsgManager(Activity activity, SurfaceView surfaceView, Camera.PreviewCallback mPreviewCallback) {
        this.activity = activity;
        this.surfaceView = surfaceView;
        this.mPreviewCallback = mPreviewCallback;
    }

    public void onCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        switchCamera(mFacing);
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
        if (changeSizeOrientation()) {
            layoutParams.height = (int) (surfaceView.getWidth() * 1.0 * size.width / size.height);
        } else {
            layoutParams.height = (int) (surfaceView.getWidth() * 1.0 * size.height / size.width);
        }
        surfaceView.setLayoutParams(layoutParams);
    }

    public void onStarted() {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onDestroyed() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void switchCamera() {
        if (mFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
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
            Camera.Size size;
            if (changeSizeOrientation()) {
                size = calculatePerfectSize(parameters.getSupportedPreviewSizes(), surfaceView.getHeight(), surfaceView.getWidth());
            } else {
                size = calculatePerfectSize(parameters.getSupportedPreviewSizes(), surfaceView.getWidth(), surfaceView.getHeight());
            }
            parameters.setPreviewSize(size.width, size.height);
            mCamera.setParameters(parameters);
//            mCamera.cancelAutoFocus();

            if (toStartPreview) {
                onStarted();
            }

        }
    }

    public void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
        }

    }

    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
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
                }
            }
        }
        return result;
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

}
