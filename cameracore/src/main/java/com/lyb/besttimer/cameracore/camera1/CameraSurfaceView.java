package com.lyb.besttimer.cameracore.camera1;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.lyb.besttimer.cameracore.LifeCaller;
import com.lyb.besttimer.cameracore.TouchMode;
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

    private TouchMode touchMode = TouchMode.FOCUS;

    private Pair<Pair<Float, Float>, Pair<Float, Float>> initTouch;

    private void initTouch(MotionEvent event) {
        if (event.getPointerCount() >= 2) {
            initTouch = new Pair<>(new Pair<>(event.getX(0), event.getY(0)), new Pair<>(event.getX(1), event.getY(1)));
        } else {
            initTouch = null;
        }
    }

    private int getOffsetZoom(MotionEvent event) {
        if (initTouch != null && event.getPointerCount() >= 2) {
            float dxy = getDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1)) -
                    getDistance(initTouch.first.first, initTouch.first.second, initTouch.second.first, initTouch.second.second);
            return (int) (dxy / 50);
        }
        return 0;
    }

    private float getDistance(float startX, float startY, float endX, float endY) {
        return (float) Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchMode = TouchMode.FOCUS;
                initTouch(event);
                cameraMsgManager.initZoom();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                touchMode = TouchMode.ZOOM;
                initTouch(event);
                cameraMsgManager.initZoom();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() >= 2) {
                    int offsetZoom = getOffsetZoom(event);
                    cameraMsgManager.offsetZoom(offsetZoom);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchMode == TouchMode.FOCUS) {
                    cameraMsgManager.clickShow(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                initTouch(event);
                break;
        }
        return true;
    }
}
