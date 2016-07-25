package com.lyb.besttimer.pluginwidget.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Common ScrollView
 * Created by linyibiao on 2016/7/12.
 */
public class BaseScrollView extends NestedScrollView {

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
    }

}
