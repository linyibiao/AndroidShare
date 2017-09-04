package com.lyb.besttimer.androidshare.adapter;

import android.graphics.PorterDuff;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.androidshare.view.PorterDuffView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */

public class PorterDuffAdapter extends BaseAdapter<PorterDuffAdapter.PorterDuffHolder> {

    private List<Pair<String, PorterDuff.Mode>> modes = new ArrayList<>();

    public PorterDuffAdapter(List<Pair<String, PorterDuff.Mode>> modes) {
        this.modes = modes;
    }

    @Override
    public PorterDuffHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PorterDuffHolder(new PorterDuffView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(PorterDuffHolder holder, int position) {
        holder.fillView(modes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return modes.size();
    }

    class PorterDuffHolder extends BaseHolder<Pair<String, PorterDuff.Mode>> {

        public PorterDuffHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillView(Pair<String, PorterDuff.Mode> data, int position) {
            ((PorterDuffView) itemView).setMode(data.first, data.second);
        }
    }
}
