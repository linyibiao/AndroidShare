package com.lyb.besttimer.pluginwidget.view.refreshlayout.flinghandle;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import com.lyb.besttimer.pluginwidget.view.refreshlayout.VerticalDragCallback;

/**
 * nestedscrollview滚动处理
 * Created by besttimer on 2017/10/3.
 */

public class NestedScrollViewFling implements FlingHandle.ViewFling {

    private NestedScrollView nestedScrollView;
    private VerticalDragCallback.FlingCall flingCall;

    public NestedScrollViewFling(NestedScrollView nestedScrollView) {
        this.nestedScrollView = nestedScrollView;
    }

    @Override
    public void handleFling(VerticalDragCallback.FlingCall flingCall) {
        unhandleFling();
        this.flingCall = flingCall;
        nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
    }

    @Override
    public void unhandleFling() {
        nestedScrollView.setOnScrollChangeListener(noopOnScrollChangeListener);
    }

    private NestedScrollView.OnScrollChangeListener onScrollChangeListener = new NestedScrollView.OnScrollChangeListener() {

        /**
         * 获取此前的dy,毕竟最后一个dy有可能只是消耗了一部分
         */
        private int preDy;

        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            int dy = scrollY - oldScrollY;
            if (dy < 0 && !ViewCompat.canScrollVertically(v, dy)) {
                flingCall.fling(-flighDy(dy));
            } else if (dy > 0 && !ViewCompat.canScrollVertically(v, dy)) {
                flingCall.fling(-flighDy(dy));
            }
            preDy = dy;
        }

        private int flighDy(int currDy) {
            if (preDy != 0) {
                return preDy;
            } else {
                return currDy;
            }
        }

    };

    private NestedScrollView.OnScrollChangeListener noopOnScrollChangeListener = new NestedScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        }
    };

}
