package com.lyb.besttimer.pluginwidget.view;

import android.content.Context;
import android.util.AttributeSet;

import com.lyb.besttimer.pluginwidget.view.swiperefreshlayout.XSwipeRefreshLayout;


/**
 * Common SwipeRefreshLayout
 * Created by linyibiao on 2016/7/12.
 */
public class BaseSwipeRefreshLayout extends XSwipeRefreshLayout {

    public BaseSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

}
