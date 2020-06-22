package com.lyb.besttimer.pluginwidget.view.linearlayout;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 水平线性布局
 *
 * @author besttimer
 * @since 2017/11/9 19:16
 */
public class HorizontalLinearLayout extends LinearLayout {

    public HorizontalLinearLayout(Context context) {
        this(context, null);
    }

    public HorizontalLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private LinearHorizontalAdapter linearHorizontalAdapter;

    public LinearHorizontalAdapter getLinearHorizontalAdapter() {
        return linearHorizontalAdapter;
    }

    public void setAdapter(LinearHorizontalAdapter linearHorizontalAdapter) {
        this.linearHorizontalAdapter = linearHorizontalAdapter;
        if (linearHorizontalAdapter != null) {
            linearHorizontalAdapter.onAttachedToLinearLayout(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (linearHorizontalAdapter != null) {
            linearHorizontalAdapter.onAttachedToLinearLayout(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (linearHorizontalAdapter != null) {
            linearHorizontalAdapter.onDetachedFromLinearLayout(this);
        }
    }

}
