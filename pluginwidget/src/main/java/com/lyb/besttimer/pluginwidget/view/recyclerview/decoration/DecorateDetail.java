package com.lyb.besttimer.pluginwidget.view.recyclerview.decoration;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * decorate detail
 * Created by linyibiao on 2016/8/17.
 */
public interface DecorateDetail {

    void drawLeft(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom);

    void drawTop(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom);

    void drawRight(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom);

    void drawBottom(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom);

}
