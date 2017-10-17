package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 头部尾部的adapter
 * Created by linyibiao on 2017/10/17.
 */

public abstract class HeadFootAdapter extends BaseAdapter {

    private RecyclerView.Adapter coreAdapter;

    public HeadFootAdapter(RecyclerView.Adapter coreAdapter) {
        this.coreAdapter = coreAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADERTYPE) {
            return onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == FOOTERTYPE) {
            return onCreateFooterViewHolder(parent, viewType);
        } else {
            return coreAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getHeaderCount()) {
            onBindHeaderViewHolder(holder, position);
        } else if (position >= getHeaderCount() + coreAdapter.getItemCount()) {
            onBindFooterViewHolder(holder, position);
        } else {
            coreAdapter.onBindViewHolder(holder, position - getHeaderCount());
        }
    }

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract int getHeaderCount();

    public abstract int getFooterCount();

    private static int HEADERTYPE = 345;
    private static int FOOTERTYPE = 456;

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            return HEADERTYPE;
        } else if (position >= getHeaderCount() + coreAdapter.getItemCount()) {
            return FOOTERTYPE;
        } else {
            return coreAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + coreAdapter.getItemCount() + getFooterCount();
    }

}
