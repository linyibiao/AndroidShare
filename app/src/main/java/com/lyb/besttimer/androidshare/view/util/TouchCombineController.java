package com.lyb.besttimer.androidshare.view.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * 管理切换触摸事件的工具类
 * Created by 林一彪 on 2016/7/12.
 */
public class TouchCombineController {

    private TouchToAncestorController touchToAncestorController;
    private TouchToDescendantsController touchToDescendantsController;

    public TouchCombineController(View target, TouchController.OnReadyListener onReadyToAncestorListener, TouchController.OnReadyListener onReadyToDescendantsListener) {
        touchToAncestorController = new TouchToAncestorController(target, onReadyToAncestorListener);
        touchToDescendantsController = new TouchToDescendantsController(target, onReadyToDescendantsListener);
    }

    public TouchToAncestorController getTouchToAncestorController() {
        return touchToAncestorController;
    }

    public TouchToDescendantsController getTouchToDescendantsController() {
        return touchToDescendantsController;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return touchToAncestorController.onTouchEvent(event) || touchToDescendantsController.onTouchEvent(event);
    }

}
