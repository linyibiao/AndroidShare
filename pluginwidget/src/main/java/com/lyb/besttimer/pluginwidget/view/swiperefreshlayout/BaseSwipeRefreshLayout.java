package com.lyb.besttimer.pluginwidget.view.swiperefreshlayout;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * base swiperefreshlayout
 * Created by linyibiao on 2016/10/28.
 */

public class BaseSwipeRefreshLayout extends SwipeRefreshLayout {

    public BaseSwipeRefreshLayout(Context context) {
        super(context);
    }

    public BaseSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

}
