package com.lyb.besttimer.pluginwidget.view.textview;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.ColorInt;

import com.lyb.besttimer.pluginwidget.caller.DrawCaller;

import java.util.ArrayList;
import java.util.List;

/**
 * draw caller manager
 * Created by linyibiao on 2016/8/16.
 */
public class DrawCallerManager {

    private BaseTextView target;

    public DrawCallerManager(BaseTextView target) {
        this.target = target;
    }

    private List<DrawCaller> bgDrawCallers = new ArrayList<>();
    private List<DrawCaller> fgDrawCallers = new ArrayList<>();

    public void addBGDrawCaller(DrawCaller drawCaller) {
        if (!bgDrawCallers.contains(drawCaller)) {
            bgDrawCallers.add(drawCaller);
            updatePadding(drawCaller);
        }
    }

    public List<DrawCaller> getBgDrawCallers() {
        return bgDrawCallers;
    }

    public DrawCaller getBgDrawCaller() {
        if (bgDrawCallers.size() > 0) {
            return bgDrawCallers.get(0);
        }
        return null;
    }

    public void setBGDrawCaller(DrawCaller drawCaller) {
        bgDrawCallers.clear();
        bgDrawCallers.add(drawCaller);
        updatePadding(drawCaller);
    }

    public void setBGcolor(@ColorInt int color) {
        for (DrawCaller drawCaller : bgDrawCallers) {
            drawCaller.setbackgroundcolor(color);
        }
        target.invalidate();
    }

    public void addFGDrawCaller(DrawCaller drawCaller) {
        if (!fgDrawCallers.contains(drawCaller)) {
            fgDrawCallers.add(drawCaller);
            updatePadding(drawCaller);
        }
    }

    public List<DrawCaller> getFgDrawCallers() {
        return fgDrawCallers;
    }

    public DrawCaller getFgDrawCaller() {
        if (fgDrawCallers.size() > 0) {
            return fgDrawCallers.get(0);
        }
        return null;
    }

    public void setFGDrawCaller(DrawCaller drawCaller) {
        fgDrawCallers.clear();
        fgDrawCallers.add(drawCaller);
        updatePadding(drawCaller);
    }

    private void updatePadding(DrawCaller drawCaller) {
        Rect padding = drawCaller.getPadding();
        boolean changed = false;
        if (target.getPaddingLeft() < padding.left) {
            changed = true;
        }
        if (target.getPaddingTop() < padding.top) {
            changed = true;
        }
        if (target.getPaddingRight() < padding.right) {
            changed = true;
        }
        if (target.getPaddingBottom() < padding.bottom) {
            changed = true;
        }

        if (changed) {
            target.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }
    }

    public void onDrawBG(Canvas canvas) {
        for (DrawCaller drawCaller : bgDrawCallers) {
            drawCaller.ondraw(canvas);
        }
    }

    public void onDrawFG(Canvas canvas) {
        for (DrawCaller drawCaller : fgDrawCallers) {
            drawCaller.ondraw(canvas);
        }
    }

}
