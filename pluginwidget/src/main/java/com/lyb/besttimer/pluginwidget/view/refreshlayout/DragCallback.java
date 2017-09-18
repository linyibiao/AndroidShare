package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;

/**
 * 自定义下拉刷新drag回调
 * Created by besttimer on 2017/9/18.
 */
class DragCallback extends ViewDragHelper.Callback {

    private RefreshLayout refreshLayout;
    private ViewDragHelper viewDragHelper;

    public DragCallback(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public void setViewDragHelper(ViewDragHelper viewDragHelper) {
        this.viewDragHelper = viewDragHelper;
    }

    enum DRAG_STATE {
        NONE,
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return true;
    }

    @Override
    public int getViewVerticalDragRange(View child) {
        return child.getHeight();
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        if (dy > 0 && ViewCompat.canScrollVertically(child, -1)) {
            return top - dy;
        }
        double factor = dy > 0 ? 3 : 4;
        double h = child.getHeight() / 8;
        double H = refreshLayout.getHeight();
        double preTop = top - dy;
        preTop = getValueX(H, h, factor, preTop);
        double currTop = preTop + dy;
        if (currTop < 0) {
            currTop = 0;
        }
        double finalTop = getValueY(H, h, factor, currTop);
        return (int) Math.ceil(finalTop);
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
        super.onViewReleased(releasedChild, xvel, yvel);
        int currTop = releasedChild.getTop();
        int thresholdTop = releasedChild.getHeight() / 8;

        int top;
        top = currTop >= thresholdTop ? thresholdTop : 0;

        viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
        refreshLayout.invalidate();
    }

    /**
     * h=-k*k/(H/factor+k)+k)
     * x=-k*k/(y-k)-k
     */
    private double getValueX(double H, double h, double factor, double y) {
        double k = H * h / (H - factor * h);
        return getFormulaX(k, y);
    }

    /**
     * h=-k*k/(H/factor+k)+k)
     * y=-k*k/(x+k)+k
     */
    private double getValueY(double H, double h, double factor, double x) {
        double k = H * h / (H - factor * h);
        return getFormulaY(k, x);
    }

    private double getFormulaX(double k, double y) {
        return k * y / (k - y);
    }

    private double getFormulaY(double k, double x) {
        return k * x / (k + x);
    }

}
