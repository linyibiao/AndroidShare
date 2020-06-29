package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

/**
 * 滚动帮助类
 * Created by linyibiao on 2017/10/4.
 */

public class ViewScrollHelper {

    /**
     * 是否开启嵌套滚动属性
     *
     * @param v 目标
     * @param x x坐标，以v的坐标系为标准
     * @param y y坐标，以v的坐标系为标准
     * @return 是否开启嵌套滚动属性
     */
    public boolean isNestedScrollingEnabled(View v, float x, float y) {
        if (isViewInTouchRange(v, x, y)) {
            if (ViewCompat.isNestedScrollingEnabled(v)) {
                return true;
            }
            if (v instanceof ViewGroup) {
                final ViewGroup group = (ViewGroup) v;
                final int scrollX = v.getScrollX();
                final int scrollY = v.getScrollY();
                final int count = group.getChildCount();
                for (int i = count - 1; i >= 0; i--) {
                    final View child = group.getChildAt(i);
                    final float childX = child.getLeft();
                    final float childY = child.getTop();
                    if (isNestedScrollingEnabled(child, x + scrollX - childX, y + scrollY - childY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否可以滚动
     *
     * @param v 目标
     * @param x x坐标，以v的坐标系为标准
     * @param y y坐标，以v的坐标系为标准
     * @return 是否可以滚动
     */
    public boolean canScrollVertically(View v, float x, float y) {
        if (isViewInTouchRange(v, x, y)) {
            if (ViewCompat.canScrollVertically(v, -1) || ViewCompat.canScrollVertically(v, 1)) {
                return true;
            }
            if (v instanceof ViewGroup) {
                final ViewGroup group = (ViewGroup) v;
                final int scrollX = v.getScrollX();
                final int scrollY = v.getScrollY();
                final int count = group.getChildCount();
                for (int i = count - 1; i >= 0; i--) {
                    final View child = group.getChildAt(i);
                    final float childX = child.getLeft();
                    final float childY = child.getTop();
                    if (canScrollVertically(child, x + scrollX - childX, y + scrollY - childY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 坐标是否在view的范围内
     *
     * @param v 目标view
     * @param x 坐标x
     * @param y 坐标y
     * @return 坐标是否在view的范围内
     */
    public boolean isViewInTouchRange(View v, float x, float y) {
        return x >= 0 && x < v.getWidth()
                && y >= 0 && y < v.getHeight();
    }

}
