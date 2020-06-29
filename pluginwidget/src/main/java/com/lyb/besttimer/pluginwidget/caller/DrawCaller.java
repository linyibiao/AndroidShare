package com.lyb.besttimer.pluginwidget.caller;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.ColorInt;

/**
 * draw callback
 * Created by linyibiao on 2016/8/16.
 */
public interface DrawCaller {

    Rect getPadding();

    void setbackgroundcolor(@ColorInt int color);

    void ondraw(Canvas canvas);

}
