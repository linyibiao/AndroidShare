package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.HeadFootAdapter;

import java.util.ArrayList;
import java.util.List;

public class HeadFootAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_foot_adapter);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_headfoot);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HFAdapter(new LVAdapter()));
    }

    private static class HFAdapter extends HeadFootAdapter {

        public HFAdapter(RecyclerView.Adapter coreAdapter) {
            super(coreAdapter);
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_level0, parent, false)) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_level1, parent, false)) {
            };
        }

        @Override
        public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getHeaderCount() {
            return 3;
        }

        @Override
        public int getFooterCount() {
            return 6;
        }
    }

    private static class LVAdapter extends RecyclerView.Adapter<LVAdapter.LVHolder> {

        private List<String> strings = new ArrayList<>();

        {
            for (int i = 0; i < 20; i++) {
                strings.add("内容" + i);
            }
        }

        @Override
        public LVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LVHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_main, parent, false));
        }

        @Override
        public void onBindViewHolder(LVHolder holder, int position) {
            holder.tv.setText(strings.get(position));
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        class LVHolder extends RecyclerView.ViewHolder {

            public TextView tv;

            public LVHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }

}
