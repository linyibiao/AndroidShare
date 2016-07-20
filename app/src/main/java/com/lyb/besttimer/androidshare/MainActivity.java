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

import com.lyb.besttimer.androidshare.activity.HeaderRecycleViewActivity;
import com.lyb.besttimer.androidshare.activity.RecycleViewActivity;
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
        lvDatas.add(new LVData(RecycleViewActivity.class, "RecycleView上下拉刷新"));
        lvDatas.add(new LVData(HeaderRecycleViewActivity.class, "RecycleView悬浮header"));
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
