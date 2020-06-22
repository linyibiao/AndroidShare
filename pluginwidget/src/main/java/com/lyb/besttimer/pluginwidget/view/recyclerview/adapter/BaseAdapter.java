package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * base adapter
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseAdapter<H extends BaseHolder> extends RecyclerView.Adapter<H> {

    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

}
