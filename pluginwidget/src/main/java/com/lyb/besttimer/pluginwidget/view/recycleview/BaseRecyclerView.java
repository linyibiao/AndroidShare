package com.lyb.besttimer.pluginwidget.view.recycleview;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Common RecycleView
 * Created by linyibiao on 2016/7/18.
 */
public class BaseRecyclerView extends RecyclerView {

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    public void dispatchOnItemTouch(int action) {
        for (RecyclerView.OnItemTouchListener onItemTouchListener : mOnItemTouchListeners) {
            onItemTouchListener.onTouchEvent(this, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), action, 0, 0, 0));
        }
    }

    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners =
            new ArrayList<>();

    public ArrayList<OnItemTouchListener> getmOnItemTouchListeners() {
        return mOnItemTouchListeners;
    }

    @Override
    public void addOnItemTouchListener(OnItemTouchListener listener) {
        super.addOnItemTouchListener(listener);
        mOnItemTouchListeners.add(listener);
    }

    @Override
    public void removeOnItemTouchListener(OnItemTouchListener listener) {
        super.removeOnItemTouchListener(listener);
        mOnItemTouchListeners.remove(listener);
    }

}
