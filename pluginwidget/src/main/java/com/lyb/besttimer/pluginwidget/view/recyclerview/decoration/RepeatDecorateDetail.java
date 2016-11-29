package com.lyb.besttimer.pluginwidget.view.recyclerview.decoration;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

/**
 * 虚线decoration
 * Created by linyibiao on 2016/11/29.
 */

public class RepeatDecorateDetail implements DecorateDetail {

    private BitmapDrawable bitmapDrawable;

    public RepeatDecorateDetail(Resources res, int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, id);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        this.bitmapDrawable = drawable;
    }

    @Override
    public void drawLeft(Canvas c, int left, int top, int right, int bottom) {
        bitmapDrawable.setBounds(left, top, right, bottom);
        bitmapDrawable.draw(c);
    }

    @Override
    public void drawTop(Canvas c, int left, int top, int right, int bottom) {
        bitmapDrawable.setBounds(left, top, right, bottom);
        bitmapDrawable.draw(c);
    }

    @Override
    public void drawRight(Canvas c, int left, int top, int right, int bottom) {
        bitmapDrawable.setBounds(left, top, right, bottom);
        bitmapDrawable.draw(c);
    }

    @Override
    public void drawBottom(Canvas c, int left, int top, int right, int bottom) {
        bitmapDrawable.setBounds(left, top, right, bottom);
        bitmapDrawable.draw(c);
    }

}
