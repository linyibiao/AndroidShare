package com.lyb.besttimer.pluginwidget.view.recyclerview.decoration;

import android.graphics.Canvas;

/**
 * decorate detail
 * Created by linyibiao on 2016/8/17.
 */
public interface DecorateDetail {

    void drawLeft(Canvas c, int left, int top, int right, int bottom);

    void drawTop(Canvas c, int left, int top, int right, int bottom);

    void drawRight(Canvas c, int left, int top, int right, int bottom);

    void drawBottom(Canvas c, int left, int top, int right, int bottom);

}
