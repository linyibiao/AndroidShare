package com.lyb.besttimer.pluginwidget.view.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Tools for managing switch touch events
 * Created by linyibiao on 2016/7/12.
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
