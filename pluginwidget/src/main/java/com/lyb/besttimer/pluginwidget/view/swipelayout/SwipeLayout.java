package com.lyb.besttimer.pluginwidget.view.swipelayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.lyb.besttimer.pluginwidget.view.recycleview.BaseRecyclerView;

/**
 * swipe layout
 * Created by linyibiao on 2016/8/26.
 */
public class SwipeLayout extends ViewGroup {

    private ViewDragHelper viewDragHelper;

    private boolean isLeftPos = true;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private SwipeOnPreDrawListener swipeOnPreDrawListener;
    private RecyclerView menuLayout;
    private MenuAdapter menuAdapter;
    private RecyclerView.Adapter<?> realAdapter;

    private boolean toNormalFromNothing = false;

    private void init(Context context) {

        viewDragHelper = ViewDragHelper.create(this, 2, new SwipeCallback());

        menuLayout = new RecyclerView(context);
        menuLayout.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        menuLayout.setAdapter(menuAdapter = new MenuAdapter());
        ajustMenuLayoutManager();
        this.addView(menuLayout);
    }

    private class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private RecyclerView.Adapter realAdapter;

        public void setRealAdapter(RecyclerView.Adapter realAdapter) {
            this.realAdapter = realAdapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return realAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            realAdapter.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            return realAdapter != null ? realAdapter.getItemCount() : 0;
        }

    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        realAdapter = adapter;
    }

    private void updateMenuAdapter() {
        menuAdapter.setRealAdapter(realAdapter);
        menuAdapter.notifyDataSetChanged();
    }

    public void setLeftPos(boolean leftPos) {
        isLeftPos = leftPos;
        ajustMenuLayoutManager();
    }

    private void ajustMenuLayoutManager() {
        if (isLeftPos) {
            menuLayout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        } else {
            menuLayout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
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
            }
            return true;
        }

        private void align(View source, View target, boolean alignLeft) {
            if (alignLeft) {
                ViewCompat.offsetTopAndBottom(source, target.getTop() - source.getTop());
                ViewCompat.offsetLeftAndRight(source, target.getLeft() - source.getRight());
            } else {
                ViewCompat.offsetTopAndBottom(source, target.getTop() - source.getTop());
                ViewCompat.offsetLeftAndRight(source, target.getRight() - source.getLeft());
            }
        }

    }

    private class SwipeCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            if (realAdapter != null && realAdapter.getItemCount() > 0) {
                return 1;
            }
            return 0;
//            return menuLayout.getWidth();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            toNormalFromNothing = false;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
            int minV = viewConfiguration.getScaledMinimumFlingVelocity() * 5;
            if (Math.abs(xvel) > minV) {
                smoothSlideViewTo(xvel < 0);
            } else {
                smoothSlideViewToWithNV();
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
        smoothSlideViewToByTarget(0, 0);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        View target = getTarget();
        ViewGroup.LayoutParams targetParams = target.getLayoutParams();
        int targetWidthSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), targetParams.width);
        int targetHeightSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), targetParams.height);
        target.measure(targetWidthSpec, targetHeightSpec);

        setMeasuredDimension(target.getMeasuredWidth(), target.getMeasuredHeight());

        menuLayout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(target.getHeight(), MeasureSpec.EXACTLY));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View target = getTarget();
        target.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + target.getMeasuredWidth(), getPaddingTop() + target.getMeasuredHeight());
        menuLayout.layout(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + menuLayout.getMeasuredWidth(), getPaddingTop() + menuLayout.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (viewDragHelper.shouldInterceptTouchEvent(ev)) {
            if (getTarget().getLeft() == 0) {
                updateMenuAdapter();
            }
            clearTouchFromRecyclerView();
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
        int preState = viewDragHelper.getViewDragState();
        viewDragHelper.processTouchEvent(event);
        int currState = viewDragHelper.getViewDragState();
        if (preState != currState && currState == ViewDragHelper.STATE_DRAGGING) {
            if (getTarget().getLeft() == 0) {
                updateMenuAdapter();
            }
            clearTouchFromRecyclerView();
        }
        return true;
    }

    private void clearTouchFromRecyclerView() {
        if (getParent() instanceof BaseRecyclerView) {
            BaseRecyclerView baseRecyclerView = (BaseRecyclerView) getParent();
            baseRecyclerView.dispatchOnItemTouch(MotionEvent.ACTION_CANCEL);
        }
    }

}
