package com.lyb.besttimer.androidshare.activity.network;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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

    private static class SimpleAdapter extends BaseAdapter<SimpleAdapter.ViewHolder> {

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

        class ViewHolder extends BaseHolder<Data> {

            private ImageView iv;

            public ViewHolder(View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.iv);
            }

            @Override
            public void fillView(Data data, int position) {
                if (data.type == 0) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.transform(new RotateTransformation(180));
                    Glide.with(getRecyclerView().getContext()).load(data.resID).apply(requestOptions).into(iv);
//                    iv.setImageResource(data.resID);
                } else if (data.type == 1) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.mipmap.refresh_arrow).transforms(new CenterCrop(),new RoundedCorners(10));
                    Glide.with(getRecyclerView().getContext()).load(data.url).apply(requestOptions).into(iv);
                }
            }
        }

        private List<Data> datas = new ArrayList<>();

        public SimpleAdapter(List<Data> datas) {
            this.datas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_glide, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.fillView(datas.get(position), position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

    }

}
