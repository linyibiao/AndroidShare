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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

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
        int timeUnit = typedArray.getInteger(R.styleable.InfinityNestedScrollView_infinityScrollView_timeUnit, 50);
        typedArray.recycle();

        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Integer> e) throws Exception {
                        InfinityNestedScrollView.this.setComputeScrollCallback(new ComputeScrollCallback() {
                            @Override
                            public void callback() {
                                e.onNext(1);
                            }
                        });
                    }
                }).debounce(timeUnit, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        realScrollWork();
                    }
                });

    }

    private List<View> reSortViews = new ArrayList<>();
    private List<View> toStartViews = new ArrayList<>();
    private List<View> toEndViews = new ArrayList<>();

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

    private ComputeScrollCallback computeScrollCallback;

    private void setComputeScrollCallback(ComputeScrollCallback computeScrollCallback) {
        this.computeScrollCallback = computeScrollCallback;
    }

    private interface ComputeScrollCallback {
        void callback();
    }

    private void realScrollWork() {
        if (canScrollVertically(-1) && !canScrollVertically(1)) {
            toEndViews.clear();
            toStartViews.clear();
            for (int index = 0; index < verticalLinearLayout.getChildCount(); index++) {
                View childView = verticalLinearLayout.getChildAt(index);
                if (childView.getY() + childView.getHeight() - getScrollY() < 0) {
                    toEndViews.add(childView);
                } else {
                    toStartViews.add(childView);
                }
            }
            if (toEndViews.size() > 0) {
                Collections.sort(toEndViews, viewComparator);
                Collections.sort(toStartViews, viewComparator);
                reSortViews.clear();
                reSortViews.addAll(toStartViews);
                reSortViews.addAll(toEndViews);
                int finalScrollY = (int) (getScrollY() - reSortViews.get(0).getY());
                int lastY = 0;
                for (View view : reSortViews) {
                    view.setY(lastY);
                    lastY += view.getHeight();
                }
                scrollTo(0, finalScrollY + speed);
            } else {
                ((ScrollInfinityOperate) verticalLinearLayout.getLinearVerticalAdapter()).copyData();
            }
        } else if (canScrollVertically(1)) {
            scrollBy(0, speed);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (computeScrollCallback != null) {
            computeScrollCallback.callback();
        }
    }

    public void setAdapter(LinearVerticalAdapter linearVerticalAdapter) {
        if (!(linearVerticalAdapter instanceof ScrollInfinityOperate)) {
            throw new RuntimeException("linearVerticalAdapter 需要继承接口 ScrollInfinityOperate");
        }
        verticalLinearLayout.setAdapter(linearVerticalAdapter);
    }

    public interface ScrollInfinityOperate {
        void copyData();
    }

}
