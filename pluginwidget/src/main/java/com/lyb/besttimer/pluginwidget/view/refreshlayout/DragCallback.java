package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
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
        return viewDragHelper.getViewDragState() != ViewDragHelper.STATE_SETTLING && child == userView && headerView != null && footerView != null;
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
        if (viewDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE && child.getTop() == 0 && canScrollVertically(child)) {
            return top - dy;
        }
        return getFinalVerticalPos(top - dy, dy);
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
        super.onViewReleased(releasedChild, xvel, yvel);
        int finalTop = getFinalReleasedPos(releasedChild);
        viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);
        refreshLayout.invalidate();
    }

    private int getFinalVerticalPos(double preTop, double dy) {
        double factor = 4;
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

    private int getFinalReleasedPos(View releasedChild) {
        int currTop = releasedChild.getTop();
        int top;
        if (currTop >= 0) {
            top = currTop >= headerView.getHeight() ? headerView.getHeight() : 0;
        } else {
            top = -currTop >= footerView.getHeight() ? -footerView.getHeight() : 0;
        }
        return top;
    }

    private boolean canScrollVertically(View child) {
        final int pointerCount = currMotionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            final float x = currMotionEvent.getX(i) + refreshLayout.getScrollX() - child.getLeft();
            final float y = currMotionEvent.getY(i) + refreshLayout.getScrollY() - child.getTop();
            if (canScrollVertically(child, x, y)) {
                return true;
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
    private boolean canScrollVertically(View v, float x, float y) {
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
            if (viewDragHelper.getViewDragState() != ViewDragHelper.STATE_SETTLING) {
                int finalTop = getFinalReleasedPos(userView);
                viewDragHelper.smoothSlideViewTo(userView, userView.getLeft(), finalTop);
                refreshLayout.invalidate();
            }
        }

        @Override
        public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dyUnconsumed != 0) {
                viewDragHelper.abort();
                int finalTop = getFinalVerticalPos(userView.getTop(), -dyUnconsumed);
                ViewCompat.offsetTopAndBottom(userView, finalTop - userView.getTop());
            }
        }

        @Override
        public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
            if (userView.getTop() != 0) {
                viewDragHelper.abort();
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
            return userView.getTop() != 0;
        }

        @Override
        public int getNestedScrollAxes() {
            return nestedScrollingParentHelper.getNestedScrollAxes();
        }
    };

    public RefreshLayout.RefreshLife getRefreshLife() {
        return refreshLife;
    }

    private RefreshLayout.RefreshLife refreshLife = new RefreshLayout.RefreshLife() {
        @Override
        public int getChildDrawingOrder(int childCount, int i) {
            return i;
        }

        @Override
        public void onFinishInflate() {
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
        }
    };

    private ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            ViewCompat.offsetTopAndBottom(headerView, userView.getTop() - headerView.getHeight() - headerView.getTop());
            ViewCompat.offsetTopAndBottom(footerView, userView.getBottom() - footerView.getTop());
            return true;
        }
    };

}
