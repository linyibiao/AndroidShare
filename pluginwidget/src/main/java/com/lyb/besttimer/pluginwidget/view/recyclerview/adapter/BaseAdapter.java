package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * base adapter
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseHolder> {

    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }
}
