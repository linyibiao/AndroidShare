package com.lyb.besttimer.androidshare.adapter;

import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.utils.ColorStateListUtil;
import com.lyb.besttimer.pluginwidget.utils.ViewState;
import com.lyb.besttimer.pluginwidget.view.tablayout.adapter.BaseTabAdapter;
import com.lyb.besttimer.pluginwidget.view.tablayout.adapter.BaseTabHolder;

/**
 * Created by Administrator on 2016/11/1.
 */

public class SimpleTabAdapter extends BaseTabAdapter {

    private String[] titles;

    public SimpleTabAdapter(TabLayout tabLayout, String[] titles) {
        super(tabLayout);
        this.titles = titles;
    }

    private class TestHolder extends BaseTabHolder<String> {

        TextView item_title;
        TextView item_number;

        public TestHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_number = (TextView) itemView.findViewById(R.id.item_number);
//            item_title.setTextColor(ColorStateListUtil.getColorStateList(new ColorStateListUtil.ColorState(0xffffffff, 0xffeeeeee, android.R.attr.state_selected)));
//            item_number.setTextColor(ColorStateListUtil.getColorStateList(new ColorStateListUtil.ColorState(0xffffffff, 0xffeeeeee, android.R.attr.state_selected)));
            item_title.setTextColor(ColorStateListUtil.getColorStateList(new ViewState<>(0xffffffff, 0xff000000, android.R.attr.state_selected)));
            item_number.setTextColor(ColorStateListUtil.getColorStateList(new ViewState<>(0xffffffff, 0xff000000, android.R.attr.state_selected)));
        }

        public void fillView(String s, int position) {
            item_title.setText(s);
            item_number.append(position + "");
        }

    }

    @Override
    public BaseTabHolder onCreateViewHolder(ViewGroup parent) {
        return new TestHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tablayout, (ViewGroup) parent.getChildAt(0), false));
    }

    @Override
    public void onBindViewHolder(BaseTabHolder holder, int position) {
        holder.fillView(titles[position], position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

}
