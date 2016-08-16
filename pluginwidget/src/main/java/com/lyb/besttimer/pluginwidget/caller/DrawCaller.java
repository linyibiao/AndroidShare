package com.lyb.besttimer.pluginwidget.caller;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * draw callback
 * Created by linyibiao on 2016/8/16.
 */
public interface DrawCaller {

    Rect getPadding();

    void ondraw(Canvas canvas);

}
