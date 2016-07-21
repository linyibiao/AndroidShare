package com.lyb.besttimer.pluginwidget.view.util;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Switch touch events to subclasses and below
 * Created by linyibiao on 2016/7/11.
 */
public class TouchToDescendantsController implements TouchController {

    private View target;
    private TouchController.OnReadyListener onReadyListener;

    public TouchToDescendantsController(View target, TouchController.OnReadyListener onReadyListener) {
        this.target = target;
        this.onReadyListener = onReadyListener;
    }

    private boolean firstDispatch = true;

    public boolean isFirstDispatch() {
        return firstDispatch;
    }

    public void setFirstDispatch(boolean firstDispatch) {
        this.firstDispatch = firstDispatch;
    }

    private Float preY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                setFirstDispatch(true);
                preY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (preY == null) {
                    preY = event.getY();
                }
                float _preY = preY;
                preY = event.getY();
                if (onReadyListener != null) {
                    if (_preY < event.getY() && onReadyListener.onReadyDown(event)) {
                        return handleTouchByChildren(event);
                    }
                    if (_preY > event.getY() && onReadyListener.onReadyUp(event)) {
                        return handleTouchByChildren(event);
                    }
                    setFirstDispatch(true);
                }
                break;
            default:
//                setFirstDispatch(true);
                preY = null;
        }
        return false;
    }

    private boolean handleTouch(MotionEvent event) {
        boolean hasSuccess = false;
        ViewGroup parent = (ViewGroup) target.getParent();
        if (firstDispatch) {
            firstDispatch = false;
            MotionEvent downMotionEvent = MotionEvent.obtainNoHistory(event);
            downMotionEvent.offsetLocation(-parent.getScrollX() + target.getLeft(), -parent.getScrollY() + target.getTop());
            downMotionEvent.setAction(MotionEvent.ACTION_DOWN);
            hasSuccess = parent.dispatchTouchEvent(downMotionEvent);
        }
        MotionEvent moveMotionEvent = MotionEvent.obtainNoHistory(event);
        moveMotionEvent.offsetLocation(-parent.getScrollX() + target.getLeft(), -parent.getScrollY() + target.getTop());
        hasSuccess |= parent.dispatchTouchEvent(moveMotionEvent);
        if (hasSuccess) {
            return true;
        }
        return false;
    }

    private boolean handleTouchByChildren(MotionEvent event) {

        for (int index = ((ViewGroup) target).getChildCount() - 1; index >= 0; index--) {
            View childView = ((ViewGroup) target).getChildAt(index);

            Rect outRect = new Rect();
            childView.getHitRect(outRect);
            if (outRect.contains((int) event.getX(), (int) event.getY())) {
                boolean hasSuccess = false;
                if (firstDispatch) {
                    firstDispatch = false;
                    MotionEvent downMotionEvent = MotionEvent.obtainNoHistory(event);
                    downMotionEvent.offsetLocation(target.getScrollX() - childView.getLeft(), target.getScrollY() - childView.getTop());
                    downMotionEvent.setAction(MotionEvent.ACTION_DOWN);
                    hasSuccess = childView.dispatchTouchEvent(downMotionEvent);
                }
                MotionEvent moveMotionEvent = MotionEvent.obtainNoHistory(event);
                moveMotionEvent.offsetLocation(target.getScrollX() - childView.getLeft(), target.getScrollY() - childView.getTop());
                hasSuccess |= childView.dispatchTouchEvent(moveMotionEvent);
                if (hasSuccess) {
//                    MotionEvent downMotionEvent = MotionEvent.obtainNoHistory(event);
//                    downMotionEvent.setAction(MotionEvent.ACTION_DOWN);
//                    target.dispatchTouchEvent(downMotionEvent);
//                    return true;

                    ViewGroup parent = (ViewGroup) target.getParent();

                    MotionEvent downMotionEvent = MotionEvent.obtainNoHistory(event);
                    downMotionEvent.setAction(MotionEvent.ACTION_DOWN);
                    downMotionEvent.offsetLocation(-parent.getScrollX() + target.getLeft(), -parent.getScrollY() + target.getTop());
                    parent.dispatchTouchEvent(downMotionEvent);
                    setFirstDispatch(true);
                    preY = null;
                    return true;

                }
            }

        }

        return false;

    }

}
