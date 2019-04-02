package com.lyb.besttimer.camera1app;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

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
        cameraMsgManager = new CameraMsgManager(activity, this,null);
        WorkStateFragment.addToManager(activity.getSupportFragmentManager()).setLifeCaller(new LifeCaller() {
            @Override
            public void onCreate() {
                getHolder().addCallback(new CameraSurfaceCallback(cameraMsgManager));
            }

            @Override
            public void onResume() {
                cameraMsgManager.startPreview();
            }

            @Override
            public void onPause() {
                cameraMsgManager.stopPreview();
            }
        });
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

}
