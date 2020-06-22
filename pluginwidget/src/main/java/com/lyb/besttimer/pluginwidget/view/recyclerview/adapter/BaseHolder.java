package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * base holder
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public T data;
    public int position;

    public void fillView(T data, int position) {
        this.data = data;
        this.position = position;
    }

}
