package com.lyb.besttimer.androidshare.adapter;

import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.utils.ColorStateListHelper;
import com.lyb.besttimer.pluginwidget.view.tablayout.BaseTabLayout;

/**
 * Created by Administrator on 2016/11/1.
 */

public class TestTabAdapter extends BaseTabLayout.BaseTabAdapter<TestTabAdapter.TestHolder> {

    private String[] titles;

    public TestTabAdapter(TabLayout tabLayout, String[] titles) {
        super(tabLayout);
        this.titles = titles;
    }

    class TestHolder extends BaseTabLayout.ViewHolder {

        TextView item_title;
        TextView item_number;

        public TestHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_number = (TextView) itemView.findViewById(R.id.item_number);
            item_title.setTextColor(ColorStateListHelper.getColorStateList(new ColorStateListHelper.ColorState(0xffffffff, 0xffeeeeee, android.R.attr.state_selected)));
            item_number.setTextColor(ColorStateListHelper.getColorStateList(new ColorStateListHelper.ColorState(0xffffffff, 0xffeeeeee, android.R.attr.state_selected)));
        }

        public void fillView(String s, int position) {
            item_title.setText(s);
            item_number.append(position + "");
        }

    }

    @Override
    public TestHolder onCreateViewHolder(ViewGroup parent) {
        return new TestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tablayout, (ViewGroup) parent.getChildAt(0), false));
    }

    @Override
    public void onBindViewHolder(TestHolder holder, int position) {
        holder.fillView(titles[position], position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

}
