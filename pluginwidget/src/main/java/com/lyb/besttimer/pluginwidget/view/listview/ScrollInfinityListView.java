package com.lyb.besttimer.pluginwidget.view.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lyb.besttimer.pluginwidget.R;

/**
 * 循环滚动列表，注意他现在是不能够响应用户操作的
 *
 * @author besttimer
 * @since 2017/11/9 10:47
 */
public class ScrollInfinityListView extends ListView {

    public ScrollInfinityListView(Context context) {
        this(context, null);
    }

    public ScrollInfinityListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollInfinityListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private int totalScrollValue = 0;//统计滑动的数值，然而只是权宜之计
    private int speed;

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollInfinityListView);
        speed = typedArray.getInteger(R.styleable.ScrollInfinityListView_infinityList_speed, 1);
        typedArray.recycle();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (canScrollVertically(-1) && !canScrollVertically(1) && getFirstVisiblePosition() > 0) {
            View firstView = getChildAt(0);
            int initScroll = firstView.getTop() - getPaddingTop();
            ScrollListOperate scrollListOperate = (ScrollListOperate) getAdapter();
            scrollListOperate.exchangeData(getFirstVisiblePosition());
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();
            int rollBackValue = -totalScrollValue - initScroll + speed + speed * 2;
            smoothScrollBy(rollBackValue, 0);
        } else if (canScrollVertically(1)) {
            smoothScrollBy(speed, 0);
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!(adapter instanceof ScrollListOperate)) {
            throw new RuntimeException("adapter 需要继承接口 ScrollListOperate");
        }
        super.setAdapter(adapter);
    }

    @Override
    public void smoothScrollBy(int distance, int duration) {
        super.smoothScrollBy(distance, duration);
        totalScrollValue += distance;
    }

    public interface ScrollListOperate {
        void exchangeData(int newStartPos);
    }

}
