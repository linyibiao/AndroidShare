package com.lyb.besttimer.pluginwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.lyb.besttimer.pluginwidget.view.util.OnReadySimpleListener;
import com.lyb.besttimer.pluginwidget.view.util.TouchCombineController;


/**
 * Created by Administrator on 2016/7/14.
 */
public class BaseListView extends ListView {

    public BaseListView(Context context) {
        this(context, null);
    }

    public BaseListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        touchCombineController = new TouchCombineController(this, new OnReadySimpleListener() {
            @Override
            public boolean onReadyDown(MotionEvent event) {
                return getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0;
            }

            @Override
            public boolean onReadyUp(MotionEvent event) {
                return getLastVisiblePosition() == getCount() - 1 && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getHeight();
            }
        }, new OnReadySimpleListener());
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
