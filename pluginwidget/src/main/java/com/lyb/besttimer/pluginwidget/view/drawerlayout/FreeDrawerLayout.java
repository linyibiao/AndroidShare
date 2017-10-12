package com.lyb.besttimer.pluginwidget.view.drawerlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.lyb.besttimer.pluginwidget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 更加自由可控的侧拉菜单
 * Created by linyibiao on 2017/10/12.
 */

public class FreeDrawerLayout extends DrawerLayout {

    public FreeDrawerLayout(Context context) {
        this(context, null);
    }

    public FreeDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreeDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                for (View contentView : contentViews) {
                    final int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
                    if (gravity == Gravity.LEFT || gravity == Gravity.START) {
                        ViewCompat.offsetLeftAndRight(contentView, (int) (drawerView.getWidth() * slideOffset * contentMoveFactor - contentView.getLeft()));
                    }
                    if (gravity == Gravity.RIGHT || gravity == Gravity.END) {
                        ViewCompat.offsetLeftAndRight(contentView, (int) (-drawerView.getWidth() * slideOffset * contentMoveFactor - contentView.getLeft()));
                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        init(context, attrs);
    }

    private float contentMoveFactor;

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FreeDrawerLayout);
        contentMoveFactor = typedArray.getFloat(R.styleable.FreeDrawerLayout_freeDL_contentMoveFactor, -1);
        typedArray.recycle();
    }

    private List<View> contentViews = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentViews.clear();
        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            if (((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY) {
                contentViews.add(child);
            }
        }
    }

}
