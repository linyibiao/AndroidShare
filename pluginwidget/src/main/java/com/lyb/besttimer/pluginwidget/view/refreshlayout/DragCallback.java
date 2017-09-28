package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 自定义下拉刷新drag回调
 * Created by besttimer on 2017/9/18.
 */
class DragCallback extends ViewDragHelper.Callback {

    private final ViewGroup refreshLayout;
    private ViewDragHelper viewDragHelper;

    private final NestedScrollingParentHelper nestedScrollingParentHelper;

    private boolean enableHeader;
    private boolean enableFooter;

    private boolean initView = false;
    private View userView;
    private View headerView;
    private View footerView;

    public DragCallback(ViewGroup refreshLayout) {
        this.refreshLayout = refreshLayout;
        refreshLayout.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
        nestedScrollingParentHelper = new NestedScrollingParentHelper(refreshLayout);
    }

    public void setViewDragHelper(ViewDragHelper viewDragHelper) {
        this.viewDragHelper = viewDragHelper;
    }

    public void setEnableHeader(boolean enableHeader) {
        this.enableHeader = enableHeader;
    }

    public void setEnableFooter(boolean enableFooter) {
        this.enableFooter = enableFooter;
    }

    private MotionEvent currMotionEvent;

    public void setCurrMotionEvent(MotionEvent currMotionEvent) {
        this.currMotionEvent = currMotionEvent;
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return child == userView && headerView != null && footerView != null;
    }

    @Override
    public int getViewVerticalDragRange(View child) {
        return child.getHeight();
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        if ((nestedScrollingParentHelper.getNestedScrollAxes() & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            return top - dy;
        }
        if (viewDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE && child.getTop() == 0 && canScroll(child, -dy)) {
            return top - dy;
        }
        return getFinalVerticalPos(top - dy, dy);
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
        super.onViewReleased(releasedChild, xvel, yvel);
        finalReleased(releasedChild);
    }

    private int getFinalVerticalPos(double preTop, double dy) {
        double factor = dy > 0 ? 3 : 4;
        double h = preTop >= 0 ? headerView.getHeight() : footerView.getHeight();
        double H = refreshLayout.getHeight();
        preTop = getValueX(H, h, factor, preTop);
        double finalTop = getValueY(H, h, factor, Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, preTop + dy)));
        if (!enableHeader && finalTop > 0) {
            finalTop = 0;
        }
        if (!enableFooter && finalTop < 0) {
            finalTop = 0;
        }
        return (int) (finalTop < 0 ? Math.ceil(finalTop) : Math.floor(finalTop));
    }

    private void finalReleased(View releasedChild) {
        int currTop = releasedChild.getTop();
        int top;
        if (currTop >= 0) {
            top = currTop >= headerView.getHeight() ? headerView.getHeight() : 0;
        } else {
            top = -currTop >= footerView.getHeight() ? -footerView.getHeight() : 0;
        }

        if (viewDragHelper.getCapturedView() != null) {
            viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
        } else {
            viewDragHelper.smoothSlideViewTo(releasedChild, releasedChild.getLeft(), top);
        }
        refreshLayout.invalidate();
    }

    private boolean canScroll(View child, int direction) {
        final int pointerCount = currMotionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            final float x = currMotionEvent.getX(i) + refreshLayout.getScrollX() - child.getLeft();
            final float y = currMotionEvent.getY(i) + refreshLayout.getScrollY() - child.getTop();
            if (canScroll(child, x, y, direction)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否可以在指定方向滚动
     *
     * @param v         目标
     * @param x         x坐标，以v的坐标系为标准
     * @param y         y坐标，以v的坐标系为标准
     * @param direction 方向：负数表示向下滚动，0或正数表示向上滚动
     * @return 是否可以滚动
     */
    private boolean canScroll(View v, float x, float y, int direction) {
        if (isViewInTouchRange(v, x, y)) {
            if (ViewCompat.canScrollVertically(v, direction)) {
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
                    if (canScroll(child, x + scrollX - childX, y + scrollY - childY, direction)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isViewInTouchRange(View v, float x, float y) {
        return x >= 0 && x < v.getWidth()
                && y >= 0 && y < v.getHeight();
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

    /**
     * x=-k*k/(y-k)-k
     */
    private double getFormulaX(double k, double y) {
        if (y >= 0) {
            return k * y / (k - y);
        } else {
            return k * y / (k + y);
        }
    }

    /**
     * y=-k*k/(x+k)+k
     */
    private double getFormulaY(double k, double x) {
        if (x >= 0) {
            return k * x / (k + x);
        } else {
            return k * x / (k - x);
        }
    }

    public NestedScrollingParent getNestedScrollingParent() {
        return nestedScrollingParent;
    }

    private NestedScrollingParent nestedScrollingParent = new NestedScrollingParent() {
        @Override
        public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        }

        @Override
        public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
            nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        }

        @Override
        public void onStopNestedScroll(View target) {
            nestedScrollingParentHelper.onStopNestedScroll(target);
            finalReleased(userView);
        }

        @Override
        public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dyUnconsumed != 0) {
                int finalTop = getFinalVerticalPos(userView.getTop(), -dyUnconsumed);
                ViewCompat.offsetTopAndBottom(userView, finalTop - userView.getTop());
            }
        }

        @Override
        public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
            if (userView.getTop() != 0) {
                int finalTop = getFinalVerticalPos(userView.getTop(), -dy);
                if ((finalTop > 0 && userView.getTop() < 0) || (finalTop < 0 && userView.getTop() > 0)) {
                    finalTop = 0;
                }
                ViewCompat.offsetTopAndBottom(userView, finalTop - userView.getTop());
                consumed[1] = dy;
            }
        }

        @Override
        public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
            return false;
        }

        @Override
        public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public int getNestedScrollAxes() {
            return nestedScrollingParentHelper.getNestedScrollAxes();
        }
    };

    public ScrollingView getScrollingView() {
        return scrollingView;
    }

    private ScrollingView scrollingView = new ScrollingView() {
        @Override
        public int computeHorizontalScrollRange() {
            return refreshLayout.getWidth();
        }

        @Override
        public int computeHorizontalScrollOffset() {
            return refreshLayout.getScrollX();
        }

        @Override
        public int computeHorizontalScrollExtent() {
            return refreshLayout.getWidth();
        }

        @Override
        public int computeVerticalScrollRange() {
            return refreshLayout.getHeight();
        }

        @Override
        public int computeVerticalScrollOffset() {
            return refreshLayout.getScrollY();
        }

        @Override
        public int computeVerticalScrollExtent() {
            return refreshLayout.getHeight();
        }
    };

    private ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (!initView) {
                for (int index = 0; index < refreshLayout.getChildCount(); index++) {
                    View child = refreshLayout.getChildAt(index);
                    RefreshLayout.LayoutParams layoutParams = (RefreshLayout.LayoutParams) child.getLayoutParams();
                    if (layoutParams.header) {
                        headerView = child;
                    } else if (layoutParams.footer) {
                        footerView = child;
                    } else {
                        userView = child;
                    }
                }
                initView = true;
            }
            ViewCompat.offsetTopAndBottom(headerView, userView.getTop() - headerView.getHeight() - headerView.getTop());
            ViewCompat.offsetTopAndBottom(footerView, userView.getBottom() - footerView.getTop());
            return true;
        }
    };

}
