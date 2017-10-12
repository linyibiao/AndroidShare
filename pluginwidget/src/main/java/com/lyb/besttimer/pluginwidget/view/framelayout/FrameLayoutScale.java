package com.lyb.besttimer.pluginwidget.view.framelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lyb.besttimer.pluginwidget.R;

/**
 * 自定义长宽比的framelayout(宽度决定高度)
 * Created by linyibiao on 2017/10/12.
 */

public class FrameLayoutScale extends FrameLayout {

    public FrameLayoutScale(@NonNull Context context) {
        this(context, null);
    }

    public FrameLayoutScale(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayoutScale(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //高宽比
    private float hwfactor;

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FrameLayoutScale);
        hwfactor = typedArray.getFloat(R.styleable.FrameLayoutScale_flScale_hwfactor, -1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (hwfactor > 0 && widthSpecSize > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSpecSize * hwfactor), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
