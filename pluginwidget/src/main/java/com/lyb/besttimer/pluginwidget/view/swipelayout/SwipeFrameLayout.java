package com.lyb.besttimer.pluginwidget.view.swipelayout;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

/**
 * swipe layout
 * Created by linyibiao on 2016/8/26.
 */
public class SwipeFrameLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;

    private boolean isLeftPos = true;

    public SwipeFrameLayout(Context context) {
        this(context, null);
    }

    public SwipeFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private ViewGroup menuLayout;
    private RecyclerView recyclerView;

    private SwipeOnPreDrawListener swipeOnPreDrawListener;

    private boolean toNormalFromNothing = false;

    private SwipeCallback swipeCallback;

    private void init(Context context) {

        swipeCallback = new SwipeCallback();
        viewDragHelper = ViewDragHelper.create(this, 1, swipeCallback);

        menuLayout = new FrameLayout(context);
        this.addView(menuLayout, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        menuLayout.addView(horizontalScrollView, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        horizontalScrollView.addView(recyclerView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);

    }

    public void setAdapter(final RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setLeftPos(boolean leftPos) {
        isLeftPos = leftPos;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (swipeOnPreDrawListener == null) {
            swipeOnPreDrawListener = new SwipeOnPreDrawListener();
        }
        getViewTreeObserver().addOnPreDrawListener(swipeOnPreDrawListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (swipeOnPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(swipeOnPreDrawListener);
        }
    }

    private class SwipeOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

        @Override
        public boolean onPreDraw() {
            View target = getTarget();
            if (viewDragHelper.getCapturedView() == target) {
                align(menuLayout, target, isLeftPos);
            } else if (viewDragHelper.getCapturedView() == menuLayout) {
                align(target, menuLayout, !isLeftPos);
            } else {
                align(menuLayout, target, isLeftPos);
            }
            return true;
        }

    }

    /**
     * align source to target
     *
     * @param source    source view
     * @param target    target view
     * @param alignLeft source view is align to left of target?
     */
    private void align(View source, View target, boolean alignLeft) {
        if (alignLeft) {
            ViewCompat.offsetTopAndBottom(source, target.getTop() - source.getTop());
            ViewCompat.offsetLeftAndRight(source, target.getLeft() - source.getRight());
        } else {
            ViewCompat.offsetTopAndBottom(source, target.getTop() - source.getTop());
            ViewCompat.offsetLeftAndRight(source, target.getRight() - source.getLeft());
        }
    }

    private class SwipeCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() > 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            View target = getTarget();
            int menuValue = menuLayout.getWidth() != 0 ? menuLayout.getWidth() : viewDragHelper.getTouchSlop();
            if (child == target) {
                if (isLeftPos) {
                    left = Math.max(left, 0);
                    left = Math.min(left, menuValue);
                } else {
                    left = Math.max(left, -menuValue);
                    left = Math.min(left, 0);
                }
            } else {
                if (isLeftPos) {
                    left = Math.max(left, -menuValue);
                    left = Math.min(left, 0);
                } else {
                    left = Math.max(left, target.getWidth());
                    left = Math.min(left, target.getWidth() - menuValue);
                }
            }

            return left;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() > 0) {
                return 1;
            }
            return 0;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (dx != 0 || dy != 0) {
                toNormalFromNothing = false;
            }
            View target = getTarget();
            if (changedView == target) {
                align(menuLayout, target, isLeftPos);
            } else if (changedView == menuLayout) {
                align(target, menuLayout, !isLeftPos);
            }
        }

        private int minV = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity() * 5;

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            autoSmooth(xvel);
        }

        void autoSmooth(float xvel) {
            if (Math.abs(xvel) >= minV) {
                smoothSlideViewTo(xvel < 0);
            } else {
                smoothSlideViewToWithNV();
            }
        }

        public void autoOpen() {
            if (isLeftPos) {
                autoSmooth(minV);
            } else {
                autoSmooth(-minV);
            }
        }

        private void smoothSlideViewTo(boolean velToLeft) {
            if (isLeftPos) {
                smoothSlideViewToByTarget(velToLeft ? 0 : menuLayout.getWidth(), 0);
            } else {
                smoothSlideViewToByTarget(velToLeft ? -menuLayout.getWidth() : 0, 0);
            }
        }

        private void smoothSlideViewToWithNV() {
            if (toNormalFromNothing && getTarget().getLeft() != 0) {
                smoothSlideViewToByTarget(0, 0);
            } else {
                if (isLeftPos) {
                    smoothSlideViewToByTarget(getTarget().getLeft() < menuLayout.getWidth() / 2 ? 0 : menuLayout.getWidth(), 0);
                } else {
                    smoothSlideViewToByTarget(getTarget().getLeft() > -menuLayout.getWidth() / 2 ? 0 : -menuLayout.getWidth(), 0);
                }
            }
        }

    }

    private void smoothSlideViewToByTarget(int finalLeft, int finalTop) {
        if (getTarget().getLeft() == finalLeft && getTarget().getTop() == finalTop && viewDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
            return;
        }
        viewDragHelper.smoothSlideViewTo(getTarget(), finalLeft, finalTop);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * reset state
     */
    public void reset() {
        reset(true);
    }

    /**
     * reset state
     */
    public void reset(boolean hasAnimation) {
        if (hasAnimation) {
            smoothSlideViewToByTarget(0, 0);
        } else {
            ViewCompat.offsetLeftAndRight(getTarget(), 0 - getTarget().getLeft());
            ViewCompat.offsetTopAndBottom(getTarget(), 0 - getTarget().getTop());
        }
    }

    /**
     * open state
     */
    public void open() {
//        swipeCallback.autoOpen();
        smoothSlideViewToByTarget(isLeftPos ? menuLayout.getWidth() : -menuLayout.getWidth(), 0);
    }

    private View getTarget() {
        for (int index = 0; index < getChildCount(); index++) {
            View childView = getChildAt(index);
            if (childView != menuLayout) {
                return childView;
            }
        }
        throw new RuntimeException("you must have a target view");
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (viewDragHelper.shouldInterceptTouchEvent(ev)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        } else {
            if (viewDragHelper.findTopChildUnder((int) ev.getX(), (int) ev.getY()) == getTarget() && getTarget().getLeft() != 0) {
                toNormalFromNothing = true;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

}
