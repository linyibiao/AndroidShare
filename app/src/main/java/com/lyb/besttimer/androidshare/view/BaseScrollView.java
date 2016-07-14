package com.lyb.besttimer.androidshare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.lyb.besttimer.androidshare.view.util.OnReadySimpleListener;
import com.lyb.besttimer.androidshare.view.util.TouchCombineController;

/**
 * 通用ScrollView
 * Created by 林一彪 on 2016/7/12.
 */
public class BaseScrollView extends ScrollView {

    public BaseScrollView(Context context) {
        this(context, null);
    }

    public BaseScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        touchCombineController = new TouchCombineController(this, new OnReadySimpleListener() {

            @Override
            public boolean onReadyDown(MotionEvent event) {
                return getScrollY() <= 0;
            }

            @Override
            public boolean onReadyUp(MotionEvent event) {
                return getChildAt(0).getHeight() - getHeight() <= getScrollY();
            }

        }, new OnReadySimpleListener() {
            @Override
            public boolean onReadyDown(MotionEvent event) {
                return getScrollY() <= 0;
            }

            @Override
            public boolean onReadyUp(MotionEvent event) {
                return getChildAt(0).getHeight() - getHeight() <= getScrollY();
            }
        });
    }


    private TouchCombineController touchCombineController;

    private float preY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (preY > ev.getY() && getChildAt(0).getHeight() - getHeight() <= getScrollY()) {
                    return false;
                } else if (preY < ev.getY() && getScrollY() <= 0) {
                    return false;
                }
                break;
        }
        preY = ev.getY();
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (touchCombineController.onTouchEvent(ev)) {
            return false;
        }

//        if (getScrollY() > 0 || (getChildCount() == 1 && getChildAt(0).getHeight() - getHeight() > getScrollY())) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }

        return super.onTouchEvent(ev);
    }
}
