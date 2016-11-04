package com.lyb.besttimer.pluginwidget.view.tablayout.adapter;

import android.view.View;

import com.lyb.besttimer.pluginwidget.view.tablayout.BaseTabLayout;

/**
 * base tab holder
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseTabHolder<T> extends BaseTabLayout.ViewHolder {

    public BaseTabHolder(View itemView) {
        super(itemView);
    }

    public abstract void fillView(T data, int position);

}
