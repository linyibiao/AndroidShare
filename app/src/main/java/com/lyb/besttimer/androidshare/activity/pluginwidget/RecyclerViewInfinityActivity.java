package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.recyclerview.ScrollInfinityRecyclerView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewInfinityActivity extends AppCompatActivity {

    @BindView(R.id.lv_scrollInfinity)
    RecyclerView lv_scrollInfinity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_infinity);
        ButterKnife.bind(this);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            strings.add("string" + i);
        }
        lv_scrollInfinity.setLayoutManager(new LinearLayoutManager(this));
        lv_scrollInfinity.setAdapter(new ScrollAdapter(strings));
    }

    class ScrollAdapter extends BaseAdapter<ScrollAdapter.InfinityHolder> implements ScrollInfinityRecyclerView.ScrollListOperate {

        private List<String> strings = new ArrayList<>();

        public ScrollAdapter(List<String> strings) {
            this.strings = strings;
        }

        @Override
        public InfinityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InfinityHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(InfinityHolder holder, int position) {
            holder.fillView(strings.get(position), position);
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        @Override
        public void exchangeData(int newStartPos) {
            List<String> endStrings = new ArrayList<>(strings.subList(0, newStartPos));
            List<String> startStrings = new ArrayList<>(strings.subList(newStartPos, strings.size()));
            strings.clear();
            strings.addAll(startStrings);
            strings.addAll(endStrings);
        }

        class InfinityHolder extends BaseHolder<String> {

            @BindView(android.R.id.text1)
            TextView text1;

            public InfinityHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @Override
            public void fillView(String data, int position) {
                super.fillView(data, position);
                text1.setText(data);
            }
        }

    }
}
