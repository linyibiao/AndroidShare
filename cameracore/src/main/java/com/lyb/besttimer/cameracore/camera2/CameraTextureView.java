package com.lyb.besttimer.cameracore.camera2;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.TextureView;

import com.lyb.besttimer.cameracore.LifeCaller;
import com.lyb.besttimer.cameracore.WorkStateFragment;

public class CameraTextureView extends TextureView {
    public CameraTextureView(Context context) {
        this(context, null);
    }

    public CameraTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
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

            }

            @Override
            public void onResume() {
                cameraMsgManager.onResume();
                cameraMsgManager.registerSensorManager();
            }

            @Override
            public void onPause() {
                cameraMsgManager.onPause();
                cameraMsgManager.unregisterSensorManager();
            }
        });
    }

}
