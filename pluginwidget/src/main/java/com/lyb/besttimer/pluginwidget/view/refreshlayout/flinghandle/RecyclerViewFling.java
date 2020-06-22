package com.lyb.besttimer.pluginwidget.view.refreshlayout.flinghandle;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lyb.besttimer.pluginwidget.view.refreshlayout.VerticalDragCallback;

/**
 * recyclerview滚动处理
 * Created by besttimer on 2017/10/3.
 */

public class RecyclerViewFling implements FlingHandle.ViewFling {

    private RecyclerView recyclerView;
    private VerticalDragCallback.FlingCall flingCall;

    public RecyclerViewFling(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void handleFling(VerticalDragCallback.FlingCall flingCall) {
        unhandleFling();
        this.flingCall = flingCall;
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void unhandleFling() {
        recyclerView.removeOnScrollListener(onScrollListener);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        /**
         * 获取此前的dy,毕竟最后一个dy有可能只是消耗了一部分
         */
        private int preDy;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy < 0 && !ViewCompat.canScrollVertically(recyclerView, dy)) {
                flingCall.fling(-flighDy(dy));
            } else if (dy > 0 && !ViewCompat.canScrollVertically(recyclerView, dy)) {
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

}
