package com.lyb.besttimer.pluginwidget.view.pullrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;

import com.lyb.besttimer.pluginwidget.R;

import java.lang.reflect.Constructor;

/**
 * pull refresh view
 * Created by linyibiao on 2016/8/8.
 */
public class PullRefreshView extends ViewGroup implements NestedScrollingChild, NestedScrollingParent {

    public PullRefreshView(Context context) {
        this(context, null);
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private View mTarget;

    private PullHeaderHandle pullHeaderManager;

    private NestedScrollingChildHelper mChildHelper;

    private NestedScrollingParentHelper mParentHelper;

    private final int[] mParentScrollConsumed = new int[2];
    private boolean mNestedScrollInProgress;

    private boolean mIsBeingDragged;

    private static final int INVALID_POINTER = -1;

    private int mScrollPointerId = INVALID_POINTER;

    private int mLastMotionY;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private ScrollerCompat mScroller;

    private VelocityTracker mVelocityTracker;

    private static final float DRAG_RATE = .5f;

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedYOffset;

    private boolean hasPassNestedScroll;

    private boolean forceToRefresh;

    private final static long SUCCESS_STAY_TIME = 1000;

    private void init(Context context, AttributeSet attrs) {

        String stateNormalStr = null;
        String stateReadyStr = null;
        String stateLoadingStr = null;
        String stateSuccessStr = null;
        String stateFailStr = null;
        int arrowResId = 0;
        String pull_class = null;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullRefreshView);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.PullRefreshView_state_normal_str) {
                stateNormalStr = a.getString(attr);
            } else if (attr == R.styleable.PullRefreshView_state_ready_str) {
                stateReadyStr = a.getString(attr);
            } else if (attr == R.styleable.PullRefreshView_state_loading_str) {
                stateLoadingStr = a.getString(attr);
            } else if (attr == R.styleable.PullRefreshView_state_success_str) {
                stateSuccessStr = a.getString(attr);
            } else if (attr == R.styleable.PullRefreshView_state_fail_str) {
                stateFailStr = a.getString(attr);
            } else if (attr == R.styleable.PullRefreshView_pull_arrow) {
                arrowResId = a.getResourceId(attr, R.mipmap.refresh_arrow);
            } else if (attr == R.styleable.PullRefreshView_pull_class) {
                pull_class = a.getString(attr);
            }
        }

        a.recycle();

        if ((pullHeaderManager = getHeaderHandle(context, pull_class)) == null) {
            pullHeaderManager = new PullHeaderManager(context);
        }
        addView(pullHeaderManager.getHeaderView());

        if (!TextUtils.isEmpty(stateNormalStr)) {
            pullHeaderManager.setStateNormalStr(stateNormalStr);
        }
        if (!TextUtils.isEmpty(stateReadyStr)) {
            pullHeaderManager.setStateReadyStr(stateReadyStr);
        }
        if (!TextUtils.isEmpty(stateLoadingStr)) {
            pullHeaderManager.setStateLoadingStr(stateLoadingStr);
        }
        if (!TextUtils.isEmpty(stateSuccessStr)) {
            pullHeaderManager.setStateSuccessStr(stateSuccessStr);
        }
        if (!TextUtils.isEmpty(stateFailStr)) {
            pullHeaderManager.setStateFailStr(stateFailStr);
        }
        if (arrowResId != 0) {
            pullHeaderManager.setImageResource(arrowResId);
        }

        pullHeaderManager.updateMSG(null);

        mChildHelper = new NestedScrollingChildHelper(this);
        mParentHelper = new NestedScrollingParentHelper(this);

        setNestedScrollingEnabled(true);

        mScroller = ScrollerCompat.create(getContext(), new LinearInterpolator());

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }

    static final Class<?>[] CONSTRUCTOR_PARAMS = new Class<?>[]{
            Context.class
    };

    private static PullHeaderHandle getHeaderHandle(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        try {
            final Class<PullHeaderHandle> clazz = (Class<PullHeaderHandle>) Class.forName(name, true, context.getClassLoader());
            Constructor<PullHeaderHandle> c = clazz.getConstructor(CONSTRUCTOR_PARAMS);
            c.setAccessible(true);
            return c.newInstance(context);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureTarget();
        for (int index = 0; index < getChildCount(); index++) {
            View childView = getChildAt(index);
            int childWidthSpec;
            int childHeightSpec;
            if (childView == pullHeaderManager.getHeaderView()) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - (getPaddingLeft() + getPaddingRight()), MeasureSpec.EXACTLY);
                childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - (getPaddingTop() + getPaddingBottom()), MeasureSpec.EXACTLY);
            } else {
                ViewGroup.LayoutParams params = childView.getLayoutParams();
                childWidthSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), params.width);
                childHeightSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height);
            }
            childView.measure(childWidthSpec, childHeightSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureTarget();
        int childL = getPaddingLeft();
        int childT = getPaddingTop();
        for (int index = 0; index < getChildCount(); index++) {
            View childView = getChildAt(index);
            if (childView == pullHeaderManager.getHeaderView()) {
                childView.layout(childL, childT - childView.getMeasuredHeight(), childL + childView.getMeasuredWidth(), childT);
            } else {
                childView.layout(childL, childT, childL + childView.getMeasuredWidth(), childT + childView.getMeasuredHeight());
            }
        }
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(pullHeaderManager.getHeaderView())) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    private boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        ensureTarget();

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN || ev.getActionMasked() == MotionEvent.ACTION_UP) {
            forceToRefresh = false;
        }

        if (canChildScrollUp() || mNestedScrollInProgress || forceToRefresh) {
            return false;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE: {

                final int y = (int) ev.getY(ev.findPointerIndex(mScrollPointerId));
                final int yDiff = mLastMotionY - y;
                if ((getScrollY() >= 0 && yDiff < -mTouchSlop) || (getScrollY() < 0 && Math.abs(yDiff) > mTouchSlop)) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    mNestedYOffset = 0;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                int actionIndex = ev.getActionIndex();
                mScrollPointerId = ev.getPointerId(actionIndex);
                mLastMotionY = (int) ev.getY(mScrollPointerId);
                break;
            }

            case MotionEvent.ACTION_DOWN: {

                int actionIndex = ev.getActionIndex();
                mScrollPointerId = ev.getPointerId(actionIndex);

                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mScrollPointerId));

                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);

                mIsBeingDragged = !mScroller.isFinished();

                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                int actionIndex = ev.getActionIndex();
                int newIndex = actionIndex == 0 ? 1 : 0;
                mScrollPointerId = ev.getPointerId(newIndex);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mScrollPointerId));
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                recycleVelocityTracker();
                releaseTouch();
                stopNestedScroll();
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        initVelocityTrackerIfNotExists();

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN || ev.getActionMasked() == MotionEvent.ACTION_UP) {
            forceToRefresh = false;
        }

        if (canChildScrollUp() || forceToRefresh) {
            return false;
        }

        MotionEvent vtev = MotionEvent.obtain(ev);

        final int actionMasked = MotionEventCompat.getActionMasked(ev);

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
        vtev.offsetLocation(0, mNestedYOffset);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    return false;
                }
                if ((mIsBeingDragged = !mScroller.isFinished())) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                int actionIndex = ev.getActionIndex();
                mScrollPointerId = ev.getPointerId(actionIndex);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mScrollPointerId));
                mNestedYOffset = 0;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                int actionIndex = ev.getActionIndex();
                mScrollPointerId = ev.getPointerId(actionIndex);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mScrollPointerId));
                break;
            }

            case MotionEvent.ACTION_MOVE:

                final int y = (int) ev.getY(ev.findPointerIndex(mScrollPointerId));
                int deltaY = mLastMotionY - y;
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    vtev.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
                if (!mIsBeingDragged && ((getScrollY() >= 0 && deltaY < -mTouchSlop) || (getScrollY() < 0 && Math.abs(deltaY) > mTouchSlop))) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    mLastMotionY = y;

                    final int oldY = getScrollY();

                    scrollByOperation(deltaY);

                    final int scrolledDeltaY = getScrollY() - oldY;
                    final int unconsumedY = deltaY - scrolledDeltaY;

                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1];
                        vtev.offsetLocation(0, mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                    }

                }
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                int actionIndex = ev.getActionIndex();
                int newIndex = actionIndex == 0 ? 1 : 0;
                mScrollPointerId = ev.getPointerId(newIndex);
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mScrollPointerId));
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    velocityTracker.getYVelocity();
                    int initialVelocity = (int) velocityTracker.getYVelocity(mScrollPointerId);

                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        flingWithNestedDispatch(-initialVelocity);
                    }
                }
                mIsBeingDragged = false;
                releaseTouch();
                stopNestedScroll();
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void scrollByOperation(int deltaY) {
        if (getScrollY() + deltaY > 0) {
            deltaY = 0 - getScrollY();
        }
        scrollBy(0, deltaY > 0 ? deltaY : (int) getResult(deltaY));
        pullHeaderManager.update(getScrollX(), getScrollY());
    }

    private double getResult(double deltaY) {
        double h = getHeight() - getPaddingTop() - getPaddingBottom();
        deltaY = deltaY * ((h + getScrollY()) / h * DRAG_RATE);
        return deltaY;
    }

    private void releaseTouch() {
        if (pullHeaderManager.canScrollToTop(getScrollX(), getScrollY())) {
            springBack(getScrollX(), getScrollY(), 0, 0, 0, 0);
        } else {
            springBack(getScrollX(), getScrollY(), 0, 0, -pullHeaderManager.getThreshold(), 0);
        }
    }

    private static final float GRAVITY = 5000.0f;

    private void springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        int dx = 0;
        int dy = 0;
        if (startX < minX) {
            dx = minX - startX;
        } else if (startX > maxX) {
            dx = maxX - startX;
        }
        if (startY < minY) {
            dy = minY - startY;
        } else if (startY > maxY) {
            dy = maxY - startY;
        }
        int mDuration = (int) (1000.0 * Math.sqrt(Math.abs(2.0 * dy / GRAVITY)));
        mScroller.startScroll(startX, startY, dx, dy, mDuration);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            if (forceToRefresh || (!mIsBeingDragged && !mNestedScrollInProgress)) {
                if (pullHeaderManager.getHeaderstate() == PullHeaderHandle.HEADERSTATE.READY) {
                    pullHeaderManager.setHeaderState(PullHeaderHandle.HEADERSTATE.LOADING);
                    if (pullListener != null) {
                        pullListener.onRefresh();
                    }
                } else if (pullHeaderManager.getHeaderstate() == PullHeaderHandle.HEADERSTATE.SUCCESS || pullHeaderManager.getHeaderstate() == PullHeaderHandle.HEADERSTATE.FAIL) {
                    if (getScrollY() >= 0) {
                        pullHeaderManager.setHeaderState(PullHeaderHandle.HEADERSTATE.NORMAL);
                    } else {
                        refreshCompletedRun(pullHeaderManager.getHeaderstate() == PullHeaderHandle.HEADERSTATE.SUCCESS);
                    }
                }
            }
        }
    }

    /**
     * update message
     *
     * @param updateMSG message,hide it if null
     */
    public void updateMSG(String updateMSG) {
        pullHeaderManager.updateMSG(updateMSG);
    }

    /**
     * force to refresh
     */
    public void forceToRefresh() {
        post(new Runnable() {
            @Override
            public void run() {
                if (pullListener != null && pullHeaderManager.getHeaderstate() != PullHeaderHandle.HEADERSTATE.LOADING) {
                    pullHeaderManager.setHeaderState(PullHeaderHandle.HEADERSTATE.READY);
                    forceToRefresh = true;
                    springBack(getScrollX(), getScrollY(), 0, 0, -pullHeaderManager.getThreshold(), -pullHeaderManager.getThreshold());
                }
            }
        });
    }

    /**
     * refresh completed
     */
    public void refreshCompleted(boolean successful) {
        refreshCompletedRun(successful);
        if (pullListener != null) {
            pullListener.onRefreshCompleted(successful);
        }
    }

    private void refreshCompletedRun(boolean successful) {
        pullHeaderManager.setHeaderState(successful ? PullHeaderHandle.HEADERSTATE.SUCCESS : PullHeaderHandle.HEADERSTATE.FAIL);
        removeCallbacks(releaseRunnable);
        postDelayed(releaseRunnable, SUCCESS_STAY_TIME);
    }

    private Runnable releaseRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsBeingDragged && !mNestedScrollInProgress) {
                springBack(getScrollX(), getScrollY(), 0, 0, 0, 0);
            }
        }
    };

    private PullListener pullListener;

    public void setPullListener(PullListener pullListener) {
        this.pullListener = pullListener;
    }

    public interface PullListener {

        void onRefresh();

        //completed
        void onRefreshCompleted(boolean successful);
    }

    private void flingWithNestedDispatch(int velocityY) {
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, false);
        }
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        mNestedScrollInProgress = true;
        forceToRefresh = false;
    }

    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
        stopNestedScroll();
        mNestedScrollInProgress = false;
        hasPassNestedScroll = false;
        releaseTouch();
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        final int oldScrollY = getScrollY();
        if (!forceToRefresh) {
            scrollByOperation(dyUnconsumed);
        }
        final int myConsumed = getScrollY() - oldScrollY;
        final int myUnconsumed = dyUnconsumed - myConsumed;
        dispatchNestedScroll(dxConsumed, dxConsumed + myConsumed, dxUnconsumed, myUnconsumed, null);
        hasPassNestedScroll = true;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (!forceToRefresh) {
            if (getScrollY() < 0) {
                if ((dy < 0 && !canChildScrollUp() && hasPassNestedScroll) || dy > 0) {
                    final int oldScrollY = getScrollY();
                    scrollByOperation(dy);
                    final int myConsumed = getScrollY() - oldScrollY;
                    if (getScrollY() < 0) {
                        consumed[1] = dy;
                    } else {
                        consumed[1] = myConsumed;
                    }
                }
            }
        }
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

}
