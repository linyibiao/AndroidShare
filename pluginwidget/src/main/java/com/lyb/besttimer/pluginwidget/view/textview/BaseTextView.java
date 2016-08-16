package com.lyb.besttimer.pluginwidget.view.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lyb.besttimer.pluginwidget.caller.DrawCaller;

import java.util.ArrayList;
import java.util.List;

/**
 * Common TextView
 * Created by linyibiao on 2016/8/16.
 */
public class BaseTextView extends TextView {

    public BaseTextView(Context context) {
        this(context, null);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private List<DrawCaller> bgDrawCallers = new ArrayList<>();
    private List<DrawCaller> fgDrawCallers = new ArrayList<>();

    private void init() {
    }

    public void addBGDrawCaller(DrawCaller drawCaller) {
        if (!bgDrawCallers.contains(drawCaller)) {
            bgDrawCallers.add(drawCaller);
            updatePadding(drawCaller);
        }
    }

    public void addFGDrawCaller(DrawCaller drawCaller) {
        if (!fgDrawCallers.contains(drawCaller)) {
            fgDrawCallers.add(drawCaller);
            updatePadding(drawCaller);
        }
    }

    private void updatePadding(DrawCaller drawCaller) {
        Rect padding = drawCaller.getPadding();
        boolean changed = false;
        if (getPaddingLeft() < padding.left) {
            changed = true;
        }
        if (getPaddingTop() < padding.top) {
            changed = true;
        }
        if (getPaddingRight() < padding.right) {
            changed = true;
        }
        if (getPaddingBottom() < padding.bottom) {
            changed = true;
        }

        if (changed) {
            setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (DrawCaller drawCaller : bgDrawCallers) {
            drawCaller.ondraw(canvas);
        }
        super.onDraw(canvas);
        for (DrawCaller drawCaller : fgDrawCallers) {
            drawCaller.ondraw(canvas);
        }
    }

}
