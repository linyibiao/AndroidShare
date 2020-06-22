package com.lyb.besttimer.pluginwidget.view.refreshlayout.flinghandle;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.lyb.besttimer.pluginwidget.view.refreshlayout.VerticalDragCallback;

/**
 * 滚动handle
 * Created by besttimer on 2017/10/3.
 */

public class FlingHandle {

    private ViewFling viewFling;

    public void handleFling(View target, VerticalDragCallback.FlingCall flingCall) {
        unhandleFling();
        if (target instanceof RecyclerView) {
            viewFling = new RecyclerViewFling((RecyclerView) target);
        } else if (target instanceof NestedScrollView) {
            viewFling = new NestedScrollViewFling((NestedScrollView) target);
        }
        if (viewFling != null) {
            viewFling.handleFling(flingCall);
        }
    }

    public void unhandleFling() {
        if (viewFling != null) {
            viewFling.unhandleFling();
        }
    }

    public interface ViewFling {

        void handleFling(VerticalDragCallback.FlingCall flingCall);

        void unhandleFling();

    }

}
