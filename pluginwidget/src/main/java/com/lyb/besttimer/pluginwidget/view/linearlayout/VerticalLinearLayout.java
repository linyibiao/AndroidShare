package com.lyb.besttimer.pluginwidget.view.linearlayout;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 垂直线性布局
 *
 * @author besttimer
 * @since 2017/11/9 19:16
 */
public class VerticalLinearLayout extends LinearLayout {

    public VerticalLinearLayout(Context context) {
        this(context, null);
    }

    public VerticalLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private LinearVerticalAdapter linearVerticalAdapter;

    public LinearVerticalAdapter getLinearVerticalAdapter() {
        return linearVerticalAdapter;
    }

    public void setAdapter(LinearVerticalAdapter linearVerticalAdapter) {
        this.linearVerticalAdapter = linearVerticalAdapter;
        if (linearVerticalAdapter != null) {
            linearVerticalAdapter.onAttachedToLinearLayout(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (linearVerticalAdapter != null) {
            linearVerticalAdapter.onAttachedToLinearLayout(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (linearVerticalAdapter != null) {
            linearVerticalAdapter.onDetachedFromLinearLayout(this);
        }
    }

}
