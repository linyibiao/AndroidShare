package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;

public class RecyclerViewShowMoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_show_more);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.brv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ShowMoreAdapter());
    }

    private static class ShowMoreAdapter extends BaseAdapter<Boolean> {

        private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

        private class ShowHolder extends BaseHolder<Boolean> {

            private Button btn;
            private TextView tv;

            ShowHolder(View itemView) {
                super(itemView);
                btn = (Button) itemView.findViewById(R.id.btn);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }

            @Override
            public void fillView(final Boolean data, final int position) {
                tv.setVisibility(data ? View.VISIBLE : View.GONE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean currData = !sparseBooleanArray.get(position);
                        sparseBooleanArray.put(position, currData);
                        notifyItemChanged(position);
                        if (currData) {
                            getRecyclerView().smoothScrollToPosition(position);
                        }
                    }
                });
            }
        }

        @Override
        public BaseHolder<Boolean> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShowHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showmore, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseHolder<Boolean> holder, int position) {
            holder.fillView(sparseBooleanArray.get(position), position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }

    }

}
