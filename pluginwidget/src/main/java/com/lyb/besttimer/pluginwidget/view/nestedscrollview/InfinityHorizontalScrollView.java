package com.lyb.besttimer.pluginwidget.view.nestedscrollview;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.lyb.besttimer.pluginwidget.R;
import com.lyb.besttimer.pluginwidget.view.linearlayout.HorizontalLinearLayout;
import com.lyb.besttimer.pluginwidget.view.linearlayout.LinearHorizontalAdapter;

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
 * 水平循环滚动的ScrollView
 *
 * @author besttimer
 * @since 2017/11/9 18:40
 */
public class InfinityHorizontalScrollView extends HorizontalScrollView {

    public InfinityHorizontalScrollView(@NonNull Context context) {
        this(context, null);
    }

    public InfinityHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfinityHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private int speed;

    private HorizontalLinearLayout horizontalLinearLayout;

    private void init(Context context, AttributeSet attrs) {

        horizontalLinearLayout = new HorizontalLinearLayout(context);
        addView(horizontalLinearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InfinityHorizontalScrollView);
        speed = typedArray.getInteger(R.styleable.InfinityHorizontalScrollView_infinityHScrollView_speed, 1);
        int timeUnit = typedArray.getInteger(R.styleable.InfinityHorizontalScrollView_infinityHScrollView_timeUnit, 50);
        typedArray.recycle();

        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Integer> e) throws Exception {
                        InfinityHorizontalScrollView.this.setComputeHScrollCallback(new ComputeHScrollCallback() {
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
            if (o1.getX() < o2.getX()) {
                return -1;
            } else if (o1.getX() > o2.getX()) {
                return 1;
            }
            return 0;
        }
    };

    private ComputeHScrollCallback computeHScrollCallback;

    private void setComputeHScrollCallback(ComputeHScrollCallback computeHScrollCallback) {
        this.computeHScrollCallback = computeHScrollCallback;
    }

    private interface ComputeHScrollCallback {
        void callback();
    }

    private void realScrollWork() {
        if (canScrollHorizontally(-1) && !canScrollHorizontally(1)) {
            toEndViews.clear();
            toStartViews.clear();
            for (int index = 0; index < horizontalLinearLayout.getChildCount(); index++) {
                View childView = horizontalLinearLayout.getChildAt(index);
                if (childView.getX() + childView.getWidth() - getScrollX() < 0) {
                    toEndViews.add(childView);
                } else {
                    toStartViews.add(childView);
                }
            }
            if (toEndViews.size() > 0) {
                Collections.sort(toEndViews, viewComparator);
                Collections.sort(toStartViews, viewComparator);
                List<View> reSortViews = new ArrayList<>();
                reSortViews.addAll(toStartViews);
                reSortViews.addAll(toEndViews);
                int finalScrollX = (int) (getScrollX() - reSortViews.get(0).getX());
                int lastX = 0;
                for (View view : reSortViews) {
                    view.setX(lastX);
                    lastX += view.getWidth();
                }
                scrollTo(finalScrollX + speed, 0);
            } else {
                ((ScrollInfinityOperate) horizontalLinearLayout.getLinearHorizontalAdapter()).copyData();
            }
        } else if (canScrollHorizontally(1)) {
            scrollBy(speed, 0);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (computeHScrollCallback != null) {
            computeHScrollCallback.callback();
        }
    }

    public void setAdapter(LinearHorizontalAdapter linearHorizontalAdapter) {
        if (!(linearHorizontalAdapter instanceof ScrollInfinityOperate)) {
            throw new RuntimeException("linearVerticalAdapter 需要继承接口 ScrollInfinityOperate");
        }
        horizontalLinearLayout.setAdapter(linearHorizontalAdapter);
    }

    public interface ScrollInfinityOperate {
        void copyData();
    }

}
