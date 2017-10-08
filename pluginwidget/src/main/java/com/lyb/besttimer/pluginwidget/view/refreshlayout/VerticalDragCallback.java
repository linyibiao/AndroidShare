package com.lyb.besttimer.pluginwidget.view.refreshlayout;

import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.lyb.besttimer.pluginwidget.view.refreshlayout.flinghandle.FlingHandle;

/**
 * 自定义下拉刷新drag回调
 * Created by besttimer on 2017/9/18.
 */
public class VerticalDragCallback implements RefreshLayout.DragCall {

    private final ViewGroup refreshLayout;
    private final ViewDragHelper viewDragHelper;

    private final ViewScrollHelper viewScrollHelper = new ViewScrollHelper();
    private final DragDistanceHelper dragDistanceHelper = new DragDistanceHelper();
    //拖拽factor倍距离才能完全拉开头部或尾部
    private final double factor = 2;

    private final NestedScrollingParentHelper nestedScrollingParentHelper;
    private final NestedScrollingChildHelper nestedScrollingChildHelper;

    private boolean enableHeader;
    private boolean enableFooter;

    private View userView;
    private View headerView;
    private View footerView;

    public VerticalDragCallback(ViewGroup refreshLayout) {
        this.refreshLayout = refreshLayout;
        viewDragHelper = ViewDragHelper.create(refreshLayout, 1, callback);
        refreshLayout.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);

        nestedScrollingParentHelper = new NestedScrollingParentHelper(refreshLayout);
        nestedScrollingChildHelper = new NestedScrollingChildHelper(refreshLayout);
    }

    @Override
    public void init() {
        ViewCompat.setNestedScrollingEnabled(refreshLayout, true);
    }

    @Override
    public RefreshLayout.RefreshLife getRefreshLife() {
        return refreshLife;
    }

    @Override
    public void setEnableHeader(boolean enableHeader) {
        this.enableHeader = enableHeader;
    }

    @Override
    public void setEnableFooter(boolean enableFooter) {
        this.enableFooter = enableFooter;
    }

    @Override
    public void finishLoadMore(boolean success) {

    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

    };

    private int getEdgeDragDistance(boolean header) {
        if (header) {
            return (int) Math.floor(dragDistanceHelper.getValueY(headerView.getHeight(), factor, Integer.MAX_VALUE));
        } else {
            return (int) Math.ceil(dragDistanceHelper.getValueY(footerView.getHeight(), factor, Integer.MIN_VALUE));
        }
    }

    private int getFinalVerticalPos(double preTop, double dy) {
        double h = preTop >= 0 ? headerView.getHeight() : footerView.getHeight();
        preTop = dragDistanceHelper.getValueX(h, factor, preTop);
        double finalTop = dragDistanceHelper.getValueY(h, factor, preTop + dy);
        if (!enableHeader && finalTop > 0) {
            finalTop = 0;
        }
        if (!enableFooter && finalTop < 0) {
            finalTop = 0;
        }
        if ((preTop > 0 && finalTop < 0) || (preTop < 0 && finalTop > 0)) {
            finalTop = 0;
        }
        return (int) Math.max(getEdgeDragDistance(false), Math.min(getEdgeDragDistance(true), finalTop));
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

    private void stopScroll() {
        unhandleFling();
        flingHandling = false;
        viewDragHelper.abort();
    }

    private RefreshLayout.RefreshLife refreshLife = new RefreshLayout.RefreshLife() {

        private int[] offsetInWindow = new int[2];

        @Override
        public void computeScroll() {
            if (viewDragHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(refreshLayout);
            } else if (flingHandling) {
                flingHandling = false;
                int finalTop = 0;
                viewDragHelper.smoothSlideViewTo(userView, userView.getLeft(), finalTop);
                ViewCompat.postInvalidateOnAnimation(refreshLayout);
            }
        }

        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void dispatchDraw(Canvas canvas) {

        }

        @Override
        public int getChildDrawingOrder(int childCount, int i) {
            return i;
        }

        @Override
        public void onFinishInflate() {
            for (int index = 0; index < refreshLayout.getChildCount(); index++) {
                View child = refreshLayout.getChildAt(index);
                RefreshLayout.LayoutParams layoutParams = (RefreshLayout.LayoutParams) child.getLayoutParams();
                switch (layoutParams.viewType) {
                    case HEADER:
                        headerView = child;
                        break;
                    case FOOTER:
                        footerView = child;
                        break;
                    case CONTENT:
                        userView = child;
                        break;
                }
            }
        }

        @Override
        public void dispatchTouchEvent(MotionEvent ev) {
            final int actionMasked = MotionEventCompat.getActionMasked(ev);
            switch (actionMasked) {
                case MotionEvent.ACTION_DOWN: {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    stopNestedScroll();
                    break;
                }
            }
        }

        @Override
        public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        }

        @Override
        public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
            nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        }

        @Override
        public void onStopNestedScroll(View target) {
            nestedScrollingParentHelper.onStopNestedScroll(target);
            if (viewDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                int finalTop = getFinalReleasedPos(userView);
                viewDragHelper.smoothSlideViewTo(userView, userView.getLeft(), finalTop);
                ViewCompat.postInvalidateOnAnimation(refreshLayout);
            }
            if (ViewCompat.canScrollVertically(target, -1) || ViewCompat.canScrollVertically(target, 1)) {
                stopNestedScroll();
            }
        }

        @Override
        public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (userView.getTop() == 0) {
                dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
                if (offsetInWindow[0] == 0 && offsetInWindow[1] == 0) {
                    int finalTop = getFinalVerticalPos(userView.getTop(), -dyUnconsumed);
                    stopScroll();
                    ViewCompat.offsetTopAndBottom(userView, finalTop - userView.getTop());
                }
            } else {
                int finalTop = getFinalVerticalPos(userView.getTop(), -dyUnconsumed);
                stopScroll();
                ViewCompat.offsetTopAndBottom(userView, finalTop - userView.getTop());
            }
        }

        @Override
        public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
            if (userView.getTop() == 0) {
                dispatchNestedPreScroll(dx, dy, consumed, null);
            } else {
                int finalTop = getFinalVerticalPos(userView.getTop(), -dy);
                if ((finalTop > 0 && userView.getTop() < 0) || (finalTop < 0 && userView.getTop() > 0)) {
                    finalTop = 0;
                }
                stopScroll();
                ViewCompat.offsetTopAndBottom(userView, finalTop - userView.getTop());
                consumed[1] = dy;
            }
        }

        @Override
        public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
            handleFling(target);
            return dispatchNestedFling(velocityX, velocityY, consumed);
        }

        @Override
        public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
            return userView.getTop() != 0 || dispatchNestedPreFling(velocityX, velocityY);
        }

        @Override
        public int getNestedScrollAxes() {
            return nestedScrollingParentHelper.getNestedScrollAxes();
        }

        @Override
        public void setNestedScrollingEnabled(boolean enabled) {
            nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
        }

        @Override
        public boolean isNestedScrollingEnabled() {
            return nestedScrollingChildHelper.isNestedScrollingEnabled();
        }

        @Override
        public boolean startNestedScroll(int axes) {
            return nestedScrollingChildHelper.startNestedScroll(axes);
        }

        @Override
        public void stopNestedScroll() {
            nestedScrollingChildHelper.stopNestedScroll();
        }

        @Override
        public boolean hasNestedScrollingParent() {
            return nestedScrollingChildHelper.hasNestedScrollingParent();
        }

        @Override
        public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }

        @Override
        public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
            return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        }

        @Override
        public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
            return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
        }

        @Override
        public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
            return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
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

    private FlingHandle flingHandle = new FlingHandle();

    private boolean flingHandling = false;

    private void handleFling(View target) {
        flingHandle.handleFling(target, flingCall);
    }

    private void unhandleFling() {
        flingHandle.unhandleFling();
    }

    private FlingCall flingCall = new FlingCall() {
        @Override
        public void fling(float dy) {
            int finalTop = (int) Math.max(getEdgeDragDistance(false), Math.min(getEdgeDragDistance(true), dy));
            viewDragHelper.smoothSlideViewTo(userView, userView.getLeft(), finalTop);
            ViewCompat.postInvalidateOnAnimation(refreshLayout);
            unhandleFling();
            flingHandling = true;
        }
    };

    public interface FlingCall {
        void fling(float dy);
    }

}
