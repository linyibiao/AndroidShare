package com.lyb.besttimer.pluginwidget.view.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lyb.besttimer.pluginwidget.view.util.OnReadySimpleListener;
import com.lyb.besttimer.pluginwidget.view.util.TouchCombineController;

/**
 * Common RecycleView
 * Created by linyibiao on 2016/7/18.
 */
public class BaseRecycleView extends RecyclerView {

    public BaseRecycleView(Context context) {
        this(context, null);
    }

    public BaseRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        touchCombineController = new TouchCombineController(this, new OnReadySimpleListener() {
            @Override
            public boolean onReadyDown(MotionEvent event) {
                View firstView = getLayoutManager().findViewByPosition(0);
                return firstView != null && firstView.getTop() >= getPaddingTop();
            }

            @Override
            public boolean onReadyUp(MotionEvent event) {
                View lastView = getLayoutManager().findViewByPosition(getLayoutManager().getItemCount() - 1);
                return lastView != null && lastView.getBottom() <= getHeight() - getPaddingBottom();
            }

        }, null);
    }


    private TouchCombineController touchCombineController;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        if (touchCombineController.onTouchEvent(ev)) {
            return false;
        }

        return super.onTouchEvent(ev);
    }

}
