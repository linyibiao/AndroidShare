package com.lyb.besttimer.pluginwidget.view.util;

import android.view.MotionEvent;

/**
 * 是否准备检测触摸的监听器简单实现
 * Created by 林一彪 on 2016/7/13.
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
