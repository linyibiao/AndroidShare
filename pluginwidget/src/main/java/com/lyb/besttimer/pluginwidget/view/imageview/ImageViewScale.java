package com.lyb.besttimer.pluginwidget.view.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.lyb.besttimer.pluginwidget.R;

/**
 * 自定义长宽比的imageview
 * Created by linyibiao on 2017/10/10.
 */

public class ImageViewScale extends AppCompatImageView {

    public ImageViewScale(Context context) {
        this(context, null);
    }

    public ImageViewScale(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewScale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //高宽比
    private float hwfactor;

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewScale);
        hwfactor = typedArray.getFloat(R.styleable.ImageViewScale_ivScale_hwfactor, -1);
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
