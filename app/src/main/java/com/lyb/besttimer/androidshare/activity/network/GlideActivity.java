package com.lyb.besttimer.androidshare.activity.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.network.glide.RotateTransformation;
import com.lyb.besttimer.pluginwidget.view.recyclerview.BaseRecyclerView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;

import java.util.ArrayList;
import java.util.List;

public class GlideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        BaseRecyclerView brv = (BaseRecyclerView) findViewById(R.id.brv);
        brv.setLayoutManager(new LinearLayoutManager(this));
        List<SimpleAdapter.Data> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(new SimpleAdapter.Data("http://www.photoblog.hk/wordpress/wp-content/uploads/2014/02/yifan02.jpg"));
            datas.add(new SimpleAdapter.Data(R.mipmap.ic_launcher));
        }
        brv.setAdapter(new SimpleAdapter(datas));
    }

    private static class SimpleAdapter extends BaseAdapter<SimpleAdapter.Data> {

        public static class Data {
            public int type;//0 for resID and 1 for url
            public int resID;
            public String url;

            public Data(int resID) {
                type = 0;
                this.resID = resID;
            }

            public Data(String url) {
                type = 1;
                this.url = url;
            }
        }

        private class ViewHolder extends BaseHolder<Data> {

            private ImageView iv;

            public ViewHolder(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.iv);
            }

            @Override
            public void fillView(Data data, int position) {
                if (data.type == 0) {
                    Glide.with(getRecyclerView().getContext()).load(data.resID).transform(new RotateTransformation(getRecyclerView().getContext(), 180)).into(iv);
//                    iv.setImageResource(data.resID);
                } else if (data.type == 1) {
                    Glide.with(getRecyclerView().getContext()).load(data.url).placeholder(R.mipmap.refresh_arrow).transform(new RotateTransformation(getRecyclerView().getContext(), 180)).into(iv);
                }
            }
        }

        private List<Data> datas = new ArrayList<>();

        public SimpleAdapter(List<Data> datas) {
            this.datas = datas;
        }

        @Override
        public BaseHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_glide, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseHolder<Data> holder, int position) {
            holder.fillView(datas.get(position), position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

    }

}
