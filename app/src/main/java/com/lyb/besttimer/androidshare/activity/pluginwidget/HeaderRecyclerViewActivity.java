package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.pluginwidget.view.recyclerview.HeaderFeature;

import java.util.ArrayList;
import java.util.List;

public class HeaderRecyclerViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_recycle_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<RVDate> rvDates = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            rvDates.add(new RVDate(i + ";;;", i % 2 == 0 ? 0 : 1));
        }
        recyclerView.setAdapter(new MyAdapter(rvDates));

        new HeaderFeature(recyclerView, (FrameLayout) findViewById(R.id.rv_header), HeaderFeature.HEADER_ORIENTION.VERTICAL) {

            @Override
            public boolean isHeader(RecyclerView recyclerView, int position) {
                int type = recyclerView.getAdapter().getItemViewType(position);
                return type == 1;
            }
        }.applyFeature();

    }

    private static class RVDate {
        public String show;

        public int type;

        public RVDate(String show, int type) {
            this.show = show;
            this.type = type;
        }
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {

        private List<RVDate> rvDates = new ArrayList<>();

        public MyAdapter(List<RVDate> rvDates) {
            this.rvDates = rvDates;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_common, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            TextView textView = holder.tv;
            textView.setText(rvDates.get(position).type == 1 ? "标题" + rvDates.get(position).show : rvDates.get(position).show);
            Button btn = holder.btn;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), rvDates.get(position).show + ";;" + rvDates.get(position).type, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return rvDates.get(position).type;
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
