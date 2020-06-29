package com.lyb.besttimer.pluginwidget.view.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.lyb.besttimer.pluginwidget.R;

/**
 * 循环滚动列表
 *
 * @author besttimer
 * @since 2017/11/9 10:47
 */
public class ScrollInfinityRecyclerView extends RecyclerView {

    public ScrollInfinityRecyclerView(Context context) {
        this(context, null);
    }

    public ScrollInfinityRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollInfinityRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private int totalScrollValue = 0;//统计滑动的数值，然而只是权宜之计
    private int speed;

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollInfinityRecyclerView);
        speed = typedArray.getInteger(R.styleable.ScrollInfinityRecyclerView_infinityRecyclerView_speed, 1);
        typedArray.recycle();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (canScrollVertically(-1) && !canScrollVertically(1) && getChildAdapterPosition(getChildAt(0)) > 0) {
            View firstView = getChildAt(0);
            int initScroll = firstView.getTop() - getPaddingTop();
            ScrollListOperate scrollListOperate = (ScrollListOperate) getAdapter();
            scrollListOperate.exchangeData(getChildAdapterPosition(firstView));
            getAdapter().notifyDataSetChanged();
            int rollBackValue = -totalScrollValue - initScroll + speed + speed * 2;
            scrollBy(0, rollBackValue);
        } else if (canScrollVertically(1)) {
            scrollBy(0, speed);
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
        totalScrollValue += y;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof ScrollListOperate)) {
            throw new RuntimeException("adapter 需要继承接口 ScrollListOperate");
        }
        super.setAdapter(adapter);
    }

    public interface ScrollListOperate {
        void exchangeData(int newStartPos);
    }

}
