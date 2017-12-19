package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;

import java.util.ArrayList;
import java.util.List;

public class SnapHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_helper);

        RecyclerView recyclerView = findViewById(R.id.rv_snap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<RVDate> rvDates = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            rvDates.add(new RVDate(i + ";;;"));
        }
        recyclerView.setAdapter(new MyAdapter(rvDates));

        new LinearSnapHelper().attachToRecyclerView(recyclerView);

    }

    private static class RVDate {
        public String show;

        public RVDate(String show) {
            this.show = show;
        }

    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {

        private List<RVDate> rvDates = new ArrayList<>();

        public MyAdapter(List<RVDate> rvDates) {
            this.rvDates = rvDates;
        }

        @Override
        public MyAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_common, parent, false);
            return new MyAdapter.Holder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.Holder holder, final int position) {
            TextView textView = holder.tv;
            textView.setText(rvDates.get(position).show);
            Button btn = holder.btn;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), rvDates.get(position).show, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return rvDates.size();
        }

        protected class Holder extends RecyclerView.ViewHolder {

            public TextView tv;
            public Button btn;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
                btn = (Button) itemView.findViewById(R.id.btn);
            }
        }

    }

}
