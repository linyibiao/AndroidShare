package com.lyb.besttimer.androidshare.view.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 切换触摸事件到父类及以上的工具类
 * Created by 林一彪 on 2016/7/11.
 */
public class TouchToAncestorController implements TouchController {

    private View target;
    private OnReadyListener onReadyListener;

    public TouchToAncestorController(View target, OnReadyListener onReadyListener) {
        this.target = target;
        this.onReadyListener = onReadyListener;
    }

//    private ViewGroup customParent;
//
//    public void setCustomParent(ViewGroup customParent) {
//        this.customParent = customParent;
//    }

    private Float preY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                preY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (preY == null) {
                    preY = event.getY();
                }
                float _preY = preY;
                preY = event.getY();
                if (onReadyListener != null) {
                    if (_preY < event.getY()) {
                        return onReadyListener.onReadyDown(event) && handleTouch(event);
                    } else if (_preY > event.getY()) {
                        return onReadyListener.onReadyUp(event) && handleTouch(event);
                    }
                }
                break;
            default:
                preY = null;
        }
        return false;
    }

    private boolean handleTouch(MotionEvent event) {
//        final ViewGroup parent;
//        if (customParent != null) {
//            parent = customParent;
//        } else {
//            parent = (ViewGroup) target.getParent();
//        }
//        MotionEvent moveEvent = MotionEvent.obtainNoHistory(event);
//        float deltaX = 0;
//        float deltaY = 0;
//        for (View v = target; v != parent; v = (View) v.getParent()) {
//            deltaX += v.getLeft() - ((View) v.getParent()).getScrollX();
//            deltaY += v.getTop() - ((View) v.getParent()).getScrollY();
//        }
//        moveEvent.offsetLocation(deltaX, deltaY);
//
//        if (parent.onInterceptTouchEvent(moveEvent)) {
//            final MotionEvent downEvent = MotionEvent.obtainNoHistory(event);
//            downEvent.offsetLocation(deltaX, deltaY);
//            downEvent.setAction(MotionEvent.ACTION_DOWN);
//            target.post(new Runnable() {
//                @Override
//                public void run() {
//                    parent.dispatchTouchEvent(downEvent);
//                }
//            });
//            return true;
//        }

        View child = target;
        MotionEvent moveEvent = event;
        while (child.getParent() instanceof ViewGroup) {

            final ViewGroup parent = (ViewGroup) child.getParent();

            float deltaX = child.getLeft() - ((View) child.getParent()).getScrollX();
            float deltaY = child.getTop() - ((View) child.getParent()).getScrollY();

            moveEvent = MotionEvent.obtainNoHistory(moveEvent);

            MotionEvent savePreMoveEvent = MotionEvent.obtainNoHistory(moveEvent);

            moveEvent.offsetLocation(deltaX, deltaY);

            MotionEvent saveAfterMoveEvent = MotionEvent.obtainNoHistory(moveEvent);

            if (parent.onInterceptTouchEvent(moveEvent)) {
                final MotionEvent downEvent = MotionEvent.obtainNoHistory(savePreMoveEvent);
                downEvent.offsetLocation(deltaX, deltaY);
                downEvent.setAction(MotionEvent.ACTION_DOWN);
                parent.post(new Runnable() {
                    @Override
                    public void run() {
                        parent.dispatchTouchEvent(downEvent);
                    }
                });
                return true;
            }

            child = (View) child.getParent();
            moveEvent = saveAfterMoveEvent;

        }

        return false;
    }

}
