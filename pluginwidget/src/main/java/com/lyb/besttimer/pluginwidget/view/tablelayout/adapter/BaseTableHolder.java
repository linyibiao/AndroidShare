package com.lyb.besttimer.pluginwidget.view.tablelayout.adapter;

import android.view.View;

/**
 * base holder
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseTableHolder<T> {

    public View itemView;

    public BaseTableHolder(View itemView) {
        this.itemView = itemView;
    }

    public T data;

    public void fillView(T data, int position) {
        this.data = data;
    }

}
