package com.lyb.besttimer.pluginwidget.view.util;

import android.view.MotionEvent;

/**
 * Ready for the listener to achieve a simple touch detection
 * Created by linyibiao on 2016/7/13.
 */
public class OnReadySimpleListener implements TouchController.OnReadyListener {

    @Override
    public boolean onReadyUp(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onReadyDown(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onReadyLeft(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onReadyRight(MotionEvent event) {
        return false;
    }

}
