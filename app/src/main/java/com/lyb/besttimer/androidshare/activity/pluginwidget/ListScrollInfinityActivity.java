package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.listview.ScrollInfinityListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListScrollInfinityActivity extends AppCompatActivity {

    @BindView(R.id.lv_scrollInfinity)
    ListView lv_scrollInfinity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_scroll_infinity);
        ButterKnife.bind(this);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            strings.add("string" + i);
        }
        lv_scrollInfinity.setAdapter(new ScrollAdapter(strings));
    }

    private class ScrollAdapter extends BaseAdapter implements ScrollInfinityListView.ScrollListOperate {

        private List<String> strings = new ArrayList<>();

        public ScrollAdapter(List<String> strings) {
            this.strings = strings;
        }

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int position) {
            return strings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView text1 = view.findViewById(android.R.id.text1);
            text1.setText(strings.get(position));
            return view;
        }

        @Override
        public void exchangeData(int newStartPos) {
            List<String> endStrings = new ArrayList<>(strings.subList(0, newStartPos));
            List<String> startStrings = new ArrayList<>(strings.subList(newStartPos, strings.size()));
            strings.clear();
            strings.addAll(startStrings);
            strings.addAll(endStrings);
        }
    }

}
