package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.pluginwidget.utils.LogUtil;

/**
 * 下拉刷新控件
 * Created by besttimer on 2017/9/17.
 */

public class RefreshLayout extends ViewGroup {

    private ViewDragHelper viewDragHelper;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DragCallback dragCallback = new DragCallback();
        viewDragHelper = ViewDragHelper.create(this, 1, dragCallback);
        dragCallback.setViewDragHelper(viewDragHelper);
    }

    private class DragCallback extends ViewDragHelper.Callback {

        private ViewDragHelper viewDragHelper;

        public void setViewDragHelper(ViewDragHelper viewDragHelper) {
            this.viewDragHelper = viewDragHelper;
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
            LogUtil.logE("top,dy:" + top + "," + dy);
            double h = child.getHeight() / 8;
            double H = getHeight();
            double preTop = top - dy;
            preTop = getValueX(H, h, preTop);
            double currTop = preTop + dy;
            if (currTop < 0) {
                currTop = 0;
            }
            double finalTop = getValueY(H, h, currTop);
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
            invalidate();
        }

        private double getValueX(double totalX, double thresholdY, double currY) {
            double k = totalX * thresholdY / (totalX - 3 * thresholdY);
            return getFormulaX(k, currY);
        }

        private double getValueY(double totalX, double thresholdY, double currX) {
            double k = totalX * thresholdY / (totalX - 3 * thresholdY);
            return getFormulaY(k, currX);
        }

        private double getFormulaX(double k, double y) {
            return k * y / (k - y);
        }

        private double getFormulaY(double k, double x) {
            return k * x / (k + x);
        }

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalArgumentException(
                    "不能在具有滚动属性的父视图中使用哦");
        }
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                    widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
            final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                    heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
            child.measure(contentWidthSpec, contentHeightSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            child.layout(lp.leftMargin, lp.topMargin,
                    lp.leftMargin + child.getMeasuredWidth(),
                    lp.topMargin + child.getMeasuredHeight());

        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams
                ? new LayoutParams((LayoutParams) p)
                : p instanceof ViewGroup.MarginLayoutParams
                ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(@Px int width, @Px int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}
