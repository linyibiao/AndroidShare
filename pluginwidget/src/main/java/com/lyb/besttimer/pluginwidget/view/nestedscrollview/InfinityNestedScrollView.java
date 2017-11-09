package com.lyb.besttimer.pluginwidget.view.nestedscrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.pluginwidget.R;
import com.lyb.besttimer.pluginwidget.view.linearlayout.LinearVerticalAdapter;
import com.lyb.besttimer.pluginwidget.view.linearlayout.VerticalLinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 循环滚动的ScrollView
 *
 * @author besttimer
 * @since 2017/11/9 18:40
 */
public class InfinityNestedScrollView extends NestedScrollView {
    public InfinityNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public InfinityNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfinityNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private int speed;

    private VerticalLinearLayout verticalLinearLayout;

    private void init(Context context, AttributeSet attrs) {

        verticalLinearLayout = new VerticalLinearLayout(context);
        addView(verticalLinearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InfinityNestedScrollView);
        speed = typedArray.getInteger(R.styleable.InfinityNestedScrollView_infinityScrollView_speed, 1);
        typedArray.recycle();
    }

    private Comparator<View> viewComparator = new Comparator<View>() {
        @Override
        public int compare(View o1, View o2) {
            if (o1.getY() < o2.getY()) {
                return -1;
            } else if (o1.getY() > o2.getY()) {
                return 1;
            }
            return 0;
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (canScrollVertically(-1) && !canScrollVertically(1)) {
            List<View> toEndViews = new ArrayList<>();
            List<View> toStartViews = new ArrayList<>();
            for (int index = 0; index < verticalLinearLayout.getChildCount(); index++) {
                View childView = verticalLinearLayout.getChildAt(index);
                if (childView.getY() + childView.getHeight() - getScrollY() < 0) {
                    toEndViews.add(childView);
                } else {
                    toStartViews.add(childView);
                }
            }
            Collections.sort(toEndViews, viewComparator);
            Collections.sort(toStartViews, viewComparator);
            List<View> reSortViews = new ArrayList<>();
            reSortViews.addAll(toStartViews);
            reSortViews.addAll(toEndViews);
            int finalScrollY = (int) (getScrollY() - reSortViews.get(0).getY());
            int lastY = 0;
            for (View view : reSortViews) {
                view.setY(lastY);
                lastY += view.getHeight();
            }
            scrollTo(0, finalScrollY + speed);
        } else if (canScrollVertically(1)) {
            scrollBy(0, speed);
        }
    }

    public void setAdapter(LinearVerticalAdapter linearVerticalAdapter) {
        verticalLinearLayout.setAdapter(linearVerticalAdapter);
    }

}
