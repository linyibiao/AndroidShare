package com.lyb.besttimer.pluginwidget.view.nestedscrollview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Common ScrollView
 * Created by linyibiao on 2016/7/12.
 */
public class BaseNestedScrollView extends NestedScrollView {

    public BaseNestedScrollView(Context context) {
        this(context, null);
    }

    public BaseNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

}
