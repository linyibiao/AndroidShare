package com.lyb.besttimer.androidshare.activity;

import android.os.Bundle;
import android.support.v4.widget.XSwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.view.BaseSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class SwipeRefreshLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh_layout);
        ListView listView = (ListView) findViewById(R.id.lv);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i + ";;;");
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));

        final BaseSwipeRefreshLayout baseSwipeRefreshLayout = (BaseSwipeRefreshLayout) findViewById(R.id.swipe);
        baseSwipeRefreshLayout.setOnRefreshListener(new XSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(XSwipeRefreshLayout.DIRECTION direction) {
                if (direction == XSwipeRefreshLayout.DIRECTION.TOP) {
                    Toast.makeText(SwipeRefreshLayoutActivity.this, "下拉", Toast.LENGTH_SHORT).show();
                } else if (direction == XSwipeRefreshLayout.DIRECTION.BOTTOM) {
                    Toast.makeText(SwipeRefreshLayoutActivity.this, "上拉", Toast.LENGTH_SHORT).show();
                }
                baseSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        baseSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });

    }
}
