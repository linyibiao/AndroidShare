package com.lyb.besttimer.androidshare.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.swipelayout.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class SwipeLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_layout);
        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.sl);
        List<String> strings = new ArrayList<>();
        strings.add("what");
        strings.add("the");
        strings.add("hell");
        swipeLayout.setAdapter(new MenuAdapter(strings));
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

        private List<String> strings = new ArrayList<>();

        public MenuAdapter(List<String> strings) {
            this.strings = strings;
        }

        protected class MenuHolder extends RecyclerView.ViewHolder {

            private Button btn_str;

            public MenuHolder(View itemView) {
                super(itemView);
                btn_str = (Button) itemView.findViewById(com.lyb.besttimer.pluginwidget.R.id.btn_str);
            }

            public void fillView(String s) {
                btn_str.setText(s);
            }

        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(LayoutInflater.from(parent.getContext()).inflate(com.lyb.besttimer.pluginwidget.R.layout.swipelayout_menu_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.fillView(strings.get(position));
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

    }

}
