package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;

public class RefreshLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_refresh);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new OuterAdapter());
    }

    private class OuterAdapter extends BaseAdapter<OuterAdapter.OuterHolder> {
        @Override
        public OuterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OuterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refresh, parent, false));
        }

        @Override
        public void onBindViewHolder(OuterHolder holder, int position) {
            holder.fillView(null, position);
        }

        @Override
        public int getItemCount() {
            return 30;
        }

        class OuterHolder extends BaseHolder<Void> {

            private RecyclerView rv_childRefresh;

            OuterHolder(View itemView) {
                super(itemView);
                rv_childRefresh = (RecyclerView) itemView.findViewById(R.id.rv_childRefresh);
                rv_childRefresh.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
            }

            @Override
            public void fillView(Void data, int position) {
                super.fillView(data, position);
                rv_childRefresh.setAdapter(new InnerAdapter());
            }
        }
    }

    private class InnerAdapter extends BaseAdapter<InnerAdapter.InnerHolder> {
        @Override
        public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InnerHolder(new ImageView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(InnerHolder holder, int position) {
            holder.fillView(null, position);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class InnerHolder extends BaseHolder<Void> {

            private final ImageView imageView;

            InnerHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView;
            }

            @Override
            public void fillView(Void data, int position) {
                super.fillView(data, position);
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

}
