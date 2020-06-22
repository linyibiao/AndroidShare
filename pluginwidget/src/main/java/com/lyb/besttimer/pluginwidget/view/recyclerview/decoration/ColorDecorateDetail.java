package com.lyb.besttimer.pluginwidget.view.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

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
    public void drawLeft(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

    @Override
    public void drawTop(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

    @Override
    public void drawRight(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

    @Override
    public void drawBottom(Canvas c, View childView, RecyclerView parent, int left, int top, int right, int bottom) {
        colorDrawable.setBounds(left, top, right, bottom);
        colorDrawable.draw(c);
    }

}
