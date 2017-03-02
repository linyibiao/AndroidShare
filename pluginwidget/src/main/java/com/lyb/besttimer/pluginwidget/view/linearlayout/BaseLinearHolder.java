package com.lyb.besttimer.pluginwidget.view.linearlayout;

import android.view.View;

/**
 * base holder
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseLinearHolder<T> {

    public View itemView;

    public BaseLinearHolder(View itemView) {
        this.itemView = itemView;
    }

    public T data;
    public int position;

    public void fillView(T data, int position) {
        this.data = data;
        this.position = position;
    }

}
