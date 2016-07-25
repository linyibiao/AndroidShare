package com.lyb.besttimer.pluginwidget.view.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

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
    }

}
