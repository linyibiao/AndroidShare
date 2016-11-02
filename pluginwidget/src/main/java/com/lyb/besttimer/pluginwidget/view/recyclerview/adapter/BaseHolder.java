package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * base holder
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void fillView(T data, int position);

}
