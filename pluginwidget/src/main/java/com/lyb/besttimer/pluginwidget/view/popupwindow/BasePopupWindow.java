package com.lyb.besttimer.pluginwidget.view.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * base popupWindow
 * Created by linyibiao on 2016/12/5.
 */

public class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context) {
        this(context, null);
    }

    public BasePopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.popupWindowStyle);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasePopupWindow() {
        this(null, 0, 0);
    }

    public BasePopupWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public BasePopupWindow(int width, int height) {
        this(null, width, height);
    }

    public BasePopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

}
