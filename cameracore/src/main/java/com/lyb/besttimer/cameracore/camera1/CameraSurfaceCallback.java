package com.lyb.besttimer.cameracore.camera1;

import android.view.SurfaceHolder;

import com.lyb.besttimer.cameracore.camera1.CameraMsgManager;

public class CameraSurfaceCallback implements SurfaceHolder.Callback {

    private final CameraMsgManager cameraMsgManager;

    public CameraSurfaceCallback(CameraMsgManager cameraMsgManager) {
        this.cameraMsgManager = cameraMsgManager;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        cameraMsgManager.onCreated(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        cameraMsgManager.onStarted();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cameraMsgManager.onDestroyed();
    }

}
