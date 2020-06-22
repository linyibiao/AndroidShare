package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.linearlayout.BaseLinearHolder;
import com.lyb.besttimer.pluginwidget.view.linearlayout.LinearHorizontalAdapter;
import com.lyb.besttimer.pluginwidget.view.linearlayout.LinearVerticalAdapter;
import com.lyb.besttimer.pluginwidget.view.nestedscrollview.InfinityHorizontalScrollView;
import com.lyb.besttimer.pluginwidget.view.nestedscrollview.InfinityNestedScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfinityNestedScrollViewActivity extends AppCompatActivity {

    @BindView(R.id.infinitySV)
    InfinityNestedScrollView infinitySV;
    @BindView(R.id.infinityHSV)
    InfinityHorizontalScrollView infinityHSV;

    private List<String> strings = new ArrayList<>();

    private LinearVerticalAdapter linearVerticalAdapter;
    private LinearHorizontalAdapter linearHorizontalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinity_nested_scroll_view);
        ButterKnife.bind(this);
        for (int i = 0; i < 3; i++) {
            strings.add("string" + i);
        }
        infinitySV.setAdapter(linearVerticalAdapter = new MyVerticalAdapter(strings));
        linearVerticalAdapter.notifyDataSetChanged();
        infinityHSV.setAdapter(linearHorizontalAdapter = new MyHorizontalAdapter(strings));
        linearHorizontalAdapter.notifyDataSetChanged();
    }

    private class MyVerticalAdapter extends LinearVerticalAdapter implements InfinityNestedScrollView.ScrollInfinityOperate {

        private List<String> strings = new ArrayList<>();

        public MyVerticalAdapter(List<String> strings) {
            this.strings = strings;
        }

        @Override
        public void copyData() {
            int preSize = strings.size();
            strings.addAll(strings);
            notifyDataSetChanged(preSize, strings.size() - 1);
        }

        class Holder extends BaseLinearHolder<String> implements View.OnClickListener {

            public Holder(View itemView) {
                super(itemView);
            }

            @Override
            public void fillView(String data, int position) {
                super.fillView(data, position);
                ((TextView) itemView.findViewById(android.R.id.text1)).setText(data);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Toast.makeText(InfinityNestedScrollViewActivity.this, "data" + data + position, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onBindViewHolder(BaseLinearHolder holder, int position) {
            holder.fillView(strings.get(position), position);
        }

        @Override
        public BaseLinearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

    }

    private class MyHorizontalAdapter extends LinearHorizontalAdapter implements InfinityHorizontalScrollView.ScrollInfinityOperate {

        private List<String> strings = new ArrayList<>();

        public MyHorizontalAdapter(List<String> strings) {
            this.strings = strings;
        }

        @Override
        public void copyData() {
            int preSize = strings.size();
            strings.addAll(strings);
            notifyDataSetChanged(preSize, strings.size() - 1);
        }

        class Holder extends BaseLinearHolder<String> implements View.OnClickListener {

            public Holder(View itemView) {
                super(itemView);
            }

            @Override
            public void fillView(String data, int position) {
                super.fillView(data, position);
                ((TextView) itemView.findViewById(android.R.id.text1)).setText(data);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Toast.makeText(InfinityNestedScrollViewActivity.this, "data" + data + position, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onBindViewHolder(BaseLinearHolder holder, int position) {
            holder.fillView(strings.get(position), position);
        }

        @Override
        public BaseLinearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

    }

}
