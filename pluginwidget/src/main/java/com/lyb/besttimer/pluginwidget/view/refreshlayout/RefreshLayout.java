package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.lyb.besttimer.pluginwidget.R;

/**
 * 下拉刷新控件
 * Created by besttimer on 2017/9/17.
 */

public class RefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {

    private RefreshLife refreshLife;

    private DragCall dragCall;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setChildrenDrawingOrderEnabled(true);

        dragCall = generateDrag();
        refreshLife = dragCall.getRefreshLife();
        dragCall.init();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
        boolean enableHeader = typedArray.getBoolean(R.styleable.RefreshLayout_refresh_enableHeader, true);
        boolean enableFooter = typedArray.getBoolean(R.styleable.RefreshLayout_refresh_enableFooter, true);
        typedArray.recycle();
        setEnableHeader(enableHeader);
        setEnableFooter(enableFooter);
    }

    public DragCall generateDrag() {
        return new VerticalDragCallback(this);
    }

    public void setEnableHeader(boolean enableHeader) {
        dragCall.setEnableHeader(enableHeader);
    }

    public void setEnableFooter(boolean enableFooter) {
        dragCall.setEnableFooter(enableFooter);
    }

    public void finishLoadMore(boolean success) {
        dragCall.finishLoadMore(success);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        refreshLife.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        View userView = null;
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int contentWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            final int contentHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
            child.measure(contentWidthSpec, contentHeightSpec);
            if (lp.viewType == LayoutParams.ViewType.CONTENT) {
                userView = child;
            }
        }
        if (userView != null) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(userView.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(userView.getMeasuredHeight(), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
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
    public void draw(Canvas canvas) {
        super.draw(canvas);
        refreshLife.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        refreshLife.dispatchDraw(canvas);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return refreshLife.getChildDrawingOrder(childCount, i);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refreshLife.onFinishInflate();
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

        public enum ViewType {
            HEADER, FOOTER, CONTENT
        }

        public ViewType viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.RefreshLayout_Layout);
            String typeStr = typedArray.getString(R.styleable.RefreshLayout_Layout_refresh_view_type);
            if (TextUtils.isEmpty(typeStr) || typeStr.equals("content")) {
                viewType = ViewType.CONTENT;
            } else if (typeStr.equals("header")) {
                viewType = ViewType.HEADER;
            } else if (typeStr.equals("footer")) {
                viewType = ViewType.FOOTER;
            }
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        refreshLife.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return refreshLife.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        refreshLife.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        refreshLife.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        refreshLife.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        refreshLife.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return refreshLife.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return refreshLife.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return refreshLife.getNestedScrollAxes();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        refreshLife.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return refreshLife.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return refreshLife.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        refreshLife.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return refreshLife.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return refreshLife.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return refreshLife.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return refreshLife.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return refreshLife.dispatchNestedPreFling(velocityX, velocityY);
    }

    public interface RefreshLife {

        void computeScroll();

        void draw(Canvas canvas);

        void dispatchDraw(Canvas canvas);

        int getChildDrawingOrder(int childCount, int i);

        void onFinishInflate();

        void dispatchTouchEvent(MotionEvent ev);

        //NestedScrollingParent
        boolean onStartNestedScroll(View child, View target, int nestedScrollAxes);

        void onNestedScrollAccepted(View child, View target, int nestedScrollAxes);

        void onStopNestedScroll(View target);

        void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed);

        void onNestedPreScroll(View target, int dx, int dy, int[] consumed);

        boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed);

        boolean onNestedPreFling(View target, float velocityX, float velocityY);

        int getNestedScrollAxes();
        //~NestedScrollingParent

        //NestedScrollingChild
        void setNestedScrollingEnabled(boolean enabled);

        boolean isNestedScrollingEnabled();

        boolean startNestedScroll(int axes);

        void stopNestedScroll();

        boolean hasNestedScrollingParent();

        boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                     int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow);

        boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow);

        boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed);

        boolean dispatchNestedPreFling(float velocityX, float velocityY);
        //~NestedScrollingChild
    }

    public interface DragCall {
        void init();

        RefreshLife getRefreshLife();

        void setEnableHeader(boolean enableHeader);

        void setEnableFooter(boolean enableFooter);

        void finishLoadMore(boolean success);
    }

}
