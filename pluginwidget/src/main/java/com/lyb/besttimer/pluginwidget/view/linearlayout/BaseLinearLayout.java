package com.lyb.besttimer.pluginwidget.view.linearlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * base linearLayout
 * Created by linyibiao on 2017/2/28.
 */

public class BaseLinearLayout extends LinearLayout {

    public BaseLinearLayout(Context context) {
        super(context);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private LinearAdapter linearAdapter;

    public void setAdapter(LinearAdapter linearAdapter) {
        this.linearAdapter = linearAdapter;
        if (linearAdapter != null) {
            linearAdapter.onAttachedToLinearLayout(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (linearAdapter != null) {
            linearAdapter.onAttachedToLinearLayout(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (linearAdapter != null) {
            linearAdapter.onDetachedFromLinearLayout(this);
        }
    }

}
