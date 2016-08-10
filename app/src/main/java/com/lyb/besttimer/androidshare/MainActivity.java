package com.lyb.besttimer.androidshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.activity.HeaderRecyclerViewActivity;
import com.lyb.besttimer.androidshare.activity.ItemTouchActivity;
import com.lyb.besttimer.androidshare.activity.ItemTreeActivity;
import com.lyb.besttimer.androidshare.activity.PullRefreshActivity;
import com.lyb.besttimer.androidshare.activity.RecyclerViewActivity;
import com.lyb.besttimer.androidshare.activity.ScrollViewControlActivity;
import com.lyb.besttimer.androidshare.activity.SwipeRefreshLayoutActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.lv);
        List<LVData> lvDatas = new ArrayList<>();
        lvDatas.add(new LVData(ScrollViewControlActivity.class, "ScrollView触摸代理切换"));
        lvDatas.add(new LVData(SwipeRefreshLayoutActivity.class, "SwipeRefreshLayout上下拉刷新"));
        lvDatas.add(new LVData(RecyclerViewActivity.class, "RecyclerView上下拉刷新"));
        lvDatas.add(new LVData(HeaderRecyclerViewActivity.class, "RecyclerView悬浮header"));
        lvDatas.add(new LVData(ItemTreeActivity.class, "多层次RecyclerView"));
        lvDatas.add(new LVData(ItemTouchActivity.class, "方格移动RecyclerView"));
        lvDatas.add(new LVData(PullRefreshActivity.class, "复古的下拉刷新"));
        listView.setAdapter(new LVAdapter(lvDatas));
    }

    private class LVData {

        public Class<?> activityC;
        public String itemShow;

        public LVData(Class<?> activityC, String itemShow) {
            this.activityC = activityC;
            this.itemShow = itemShow;
        }

    }

    private class LVAdapter extends BaseAdapter {

        private List<LVData> lvDatas = new ArrayList<>();

        public LVAdapter(List<LVData> lvDatas) {
            this.lvDatas = lvDatas;
        }

        @Override
        public int getCount() {
            return lvDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return lvDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {

            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_main, parent, false);
            }

            final LVData lvData = (LVData) getItem(position);

            TextView textView = (TextView) view.findViewById(R.id.tv);
            textView.setText(lvData.itemShow);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parent.getContext(), lvData.activityC);
                    intent.putExtra("title", lvData.itemShow);
                    startActivity(intent);
                }
            });

            return view;
        }

    }

}
