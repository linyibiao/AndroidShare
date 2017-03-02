package com.lyb.besttimer.androidshare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.linearlayout.BaseLinearHolder;
import com.lyb.besttimer.pluginwidget.view.linearlayout.LinearAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class SimpleLinearAdapter extends LinearAdapter {

    private List<SimpleTableData> simpleTableDatas = new ArrayList<>();

    public SimpleLinearAdapter(TableInfo tableInfo, List<SimpleTableData> simpleTableDatas) {
        super(tableInfo);
        this.simpleTableDatas = simpleTableDatas;
    }

    public static class SimpleTableData {
        public String showContent;

        public SimpleTableData(String showContent) {
            this.showContent = showContent;
        }

    }

    private static class TableHolder extends BaseLinearHolder<SimpleTableData> {

        private TextView tv;

        public TableHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        public void fillView(SimpleTableData data, int position) {
            super.fillView(data, position);
            tv.setText(data.showContent);
        }
    }

    @Override
    public void onBindViewHolder(BaseLinearHolder holder, int position) {
        holder.fillView(simpleTableDatas.get(position), position);
    }

    @Override
    public BaseLinearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tablelayout, parent, false));
    }

    @Override
    public int getItemCount() {
        return simpleTableDatas.size();
    }

}
