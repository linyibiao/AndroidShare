package com.lyb.besttimer.cameracore.camera1;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.lyb.besttimer.cameracore.LifeCaller;
import com.lyb.besttimer.cameracore.WorkStateFragment;

public class CameraSurfaceView extends SurfaceView {
    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private CameraMsgManager cameraMsgManager;

    public CameraMsgManager getCameraMsgManager() {
        return cameraMsgManager;
    }

    private void init() {
        FragmentActivity activity = (FragmentActivity) getContext();
        cameraMsgManager = new CameraMsgManager(activity, this);
        WorkStateFragment.addToManager(activity.getSupportFragmentManager()).setLifeCaller(new LifeCaller() {
            @Override
            public void onCreate() {
                getHolder().addCallback(new CameraSurfaceCallback(cameraMsgManager));
            }

            @Override
            public void onResume() {
                cameraMsgManager.resumePreview();
                cameraMsgManager.registerSensorManager();
            }

            @Override
            public void onPause() {
                cameraMsgManager.pausePreview();
                cameraMsgManager.unregisterSensorManager();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                cameraMsgManager.clickShow(event.getX(), event.getY());
                break;
        }
        return true;
    }
}
