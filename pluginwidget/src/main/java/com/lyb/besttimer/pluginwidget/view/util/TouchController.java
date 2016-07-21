package com.lyb.besttimer.pluginwidget.view.util;

import android.view.MotionEvent;

/**
 * Touch control interface
 * Created by linyibiao on 2016/7/14.
 */
public interface TouchController {

    boolean onTouchEvent(MotionEvent event);

    /**
     * Whether to touch the listener detection
     */
    interface OnReadyListener {

        boolean onReadyUp(MotionEvent event);

        boolean onReadyDown(MotionEvent event);

        boolean onReadyLeft(MotionEvent event);

        boolean onReadyRight(MotionEvent event);

    }

}
