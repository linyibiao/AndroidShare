package com.lyb.besttimer.pluginwidget.view.recycleview.decoration;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;

/**
 * color decorate detail
 * Created by linyibiao on 2016/8/17.
 */
public class ColorDecorateDetail implements DecorateDetail {

    private ColorDrawable colorDrawable;

    public ColorDecorateDetail(int color) {
        this.colorDrawable = new ColorDrawable(color);
    }

    @Override
    public void drawLeft(Canvas c, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

    @Override
    public void drawTop(Canvas c, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

    @Override
    public void drawRight(Canvas c, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

    @Override
    public void drawBottom(Canvas c, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

}
