package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.pluginwidget.data.ItemTree;
import com.lyb.besttimer.pluginwidget.data.TreeDataManager;
import com.lyb.besttimer.pluginwidget.view.recyclerview.BaseRecyclerView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.HeaderFeature;

import java.util.ArrayList;
import java.util.List;

public class ItemTreeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_tree);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setItemAnimator(new DefaultItemAnimator(){

            @Override
            public long getChangeDuration() {
                return 3000;
            }

            @Override
            public long getMoveDuration() {
                return 3000;
            }

            @Override
            public long getRemoveDuration() {
                return 3000;
            }

            @Override
            public long getAddDuration() {
                return 3000;
            }

        });
//        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<ItemTree> itemTrees = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ItemTree itemTree0 = new ItemTree(new RVData("层次" + i, 0), true, null);
            itemTrees.add(itemTree0);
            for (int j = 0; j < 3; j++) {
                ItemTree itemTree1 = new ItemTree(new RVData("层次" + i + j, 1), true, itemTree0);
                for (int k = 0; k < 3; k++) {
                    new ItemTree(new RVData("层次" + i + j + k, 2), true, itemTree1);
                }
            }
        }
        recyclerView.setAdapter(new MyAdapter(new TreeDataManager(recyclerView, itemTrees)));

        new HeaderFeature(recyclerView, findViewById(R.id.rv_header), HeaderFeature.HEADER_ORIENTION.VERTICAL) {

            @Override
            public boolean isHeader(RecyclerView recyclerView, int position) {
                int type = recyclerView.getAdapter().getItemViewType(position);
                return type != 2;
            }
        }.applyFeature();

    }

    private static class RVData {

        public String show;

        public int type;

        public RVData(String show, int type) {
            this.show = show;
            this.type = type;
        }
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {

        private TreeDataManager treeDataManager;

        public MyAdapter(TreeDataManager treeDataManager) {
            this.treeDataManager = treeDataManager;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_list_level0, parent, false);
                    break;
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_list_level1, parent, false);
                    break;
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_list_level2, parent, false);
                    break;
            }
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            final ItemTree itemTree = treeDataManager.getItem(position);
            final RVData rvData = (RVData) itemTree.getObject();
            TextView textView = holder.tv;
            textView.setText(rvData.show);
            Button btn = holder.btn;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), rvData.show, Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    treeDataManager.flex(treeDataManager.indexOf(itemTree));
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return ((RVData) treeDataManager.getItem(position).getObject()).type;
        }

        @Override
        public int getItemCount() {
            return treeDataManager.getItemCount();
        }

        protected class Holder extends RecyclerView.ViewHolder {

            public TextView tv;
            public Button btn;
            public BaseRecyclerView brv_datas;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
                btn = (Button) itemView.findViewById(R.id.btn);
                brv_datas = (BaseRecyclerView) itemView.findViewById(R.id.brv_datas);
            }
        }

    }

}
