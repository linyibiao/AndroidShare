package com.lyb.besttimer.pluginwidget.view.viewpager;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 更加自由可控的ViewPager
 * Created by linyibiao on 2017/10/12.
 */

public class FreeViewPager extends ViewPager {

    public FreeViewPager(Context context) {
        this(context, null);
    }

    public FreeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private GestureDetectorCompat gestureDetectorCompat;

    public void setCustomSimpleOnGestureListener(GestureDetector.SimpleOnGestureListener customSimpleOnGestureListener) {
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), customSimpleOnGestureListener);
    }

    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE || ev.getAction() == MotionEvent.ACTION_UP) {
            if (((!canScrollHorizontally(-1) && ev.getX() > lastX) || (!canScrollHorizontally(1) && ev.getX() < lastX)) && gestureDetectorCompat != null) {
                return gestureDetectorCompat.onTouchEvent(ev);
            }
        }
        lastX = ev.getX();
        return super.onTouchEvent(ev);
    }
}
