package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.pluginwidget.R;

/**
 * 下拉刷新控件
 * Created by besttimer on 2017/9/17.
 */

public class RefreshLayout extends ViewGroup implements NestedScrollingParent, ScrollingView {

    private ViewDragHelper viewDragHelper;
    private DragCallback dragCallback;
    private ScrollingView scrollingView;
    private NestedScrollingParent nestedScrollingParent;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
        boolean enableHeader = typedArray.getBoolean(R.styleable.RefreshLayout_refresh_enableHeader, true);
        boolean enableFooter = typedArray.getBoolean(R.styleable.RefreshLayout_refresh_enableFooter, true);
        typedArray.recycle();
        dragCallback = new DragCallback(this);
        viewDragHelper = ViewDragHelper.create(this, 1, dragCallback);
        dragCallback.setViewDragHelper(viewDragHelper);
        scrollingView = dragCallback.getScrollingView();
        nestedScrollingParent = dragCallback.getNestedScrollingParent();
        setEnableHeader(enableHeader);
        setEnableFooter(enableFooter);
    }

    public void setEnableHeader(boolean enableHeader) {
        dragCallback.setEnableHeader(enableHeader);
    }

    public void setEnableFooter(boolean enableFooter) {
        dragCallback.setEnableFooter(enableFooter);
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
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int contentWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            final int contentHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
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
            child.layout(0, 0,
                    child.getMeasuredWidth(),
                    child.getMeasuredHeight());
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

        public boolean header;
        public boolean footer;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.RefreshLayout_Layout);
            header = typedArray.getBoolean(R.styleable.RefreshLayout_Layout_refresh_header, false);
            footer = typedArray.getBoolean(R.styleable.RefreshLayout_Layout_refresh_footer, false);
            typedArray.recycle();
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
        dragCallback.setCurrMotionEvent(ev);
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragCallback.setCurrMotionEvent(event);
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return nestedScrollingParent.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        nestedScrollingParent.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        nestedScrollingParent.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        nestedScrollingParent.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        nestedScrollingParent.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingParent.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return nestedScrollingParent.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParent.getNestedScrollAxes();
    }

    @Override
    public int computeHorizontalScrollRange() {
        return scrollingView.computeHorizontalScrollRange();
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return scrollingView.computeHorizontalScrollOffset();
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return scrollingView.computeHorizontalScrollExtent();
    }

    @Override
    public int computeVerticalScrollRange() {
        return scrollingView.computeVerticalScrollRange();
    }

    @Override
    public int computeVerticalScrollOffset() {
        return scrollingView.computeVerticalScrollOffset();
    }

    @Override
    public int computeVerticalScrollExtent() {
        return scrollingView.computeVerticalScrollExtent();
    }

}
