package com.lyb.besttimer.pluginwidget.view.textview;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Common TextView
 * Created by linyibiao on 2016/8/16.
 */
public class BaseTextView extends AppCompatTextView {

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

    private DrawCallerManager drawCallerManager;

    private void init() {
        drawCallerManager = new DrawCallerManager(this);
    }

    public DrawCallerManager getDrawCallerManager() {
        return drawCallerManager;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCallerManager.onDrawBG(canvas);
        super.onDraw(canvas);
        drawCallerManager.onDrawFG(canvas);
    }

}
