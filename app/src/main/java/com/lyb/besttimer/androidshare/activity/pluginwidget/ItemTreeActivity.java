package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.androidshare.view.SpanResBg;
import com.lyb.besttimer.pluginwidget.data.ItemTree;
import com.lyb.besttimer.pluginwidget.data.TreeDataManager;
import com.lyb.besttimer.pluginwidget.view.recyclerview.BaseRecyclerView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemTreeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_tree);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<ItemTree> itemTrees = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ItemTree itemTree0 = new ItemTree(new RVData("层次" + i, 0), true, null);
            itemTrees.add(itemTree0);
            for (int j = 0; j < 30; j++) {
                ItemTree itemTree1 = new ItemTree(new RVData("层次" + i + j, 1), true, itemTree0);
                for (int k = 0; k < 30; k++) {
                    new ItemTree(new RVData("层次" + i + j + k, 2), true, itemTree1);
                }
            }
        }
        recyclerView.setAdapter(new MyAdapter(new TreeDataManager(itemTrees)));

//        new HeaderFeature(recyclerView, (FrameLayout) findViewById(R.id.rv_header), HeaderFeature.HEADER_ORIENTION.VERTICAL) {
//
//            @Override
//            public boolean isHeader(RecyclerView recyclerView, int position) {
//                int type = recyclerView.getAdapter().getItemViewType(position);
//                return type == 1;
//            }
//        }.applyFeature();
//
//        recyclerView.addItemDecoration(new BaseItemDecoration(0, 0, 0, 100, new ColorDecorateDetail(0xaaff0000)));

    }

    private static class RVData {

        public String show;

        public int type;

        public RVData(String show, int type) {
            this.show = show;
            this.type = type;
        }
    }

    private static class MyAdapter extends BaseAdapter<MyAdapter.Holder> {

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
            holder.fillView(itemTree, position);
        }

        @Override
        public int getItemViewType(int position) {
            return ((RVData) treeDataManager.getItem(position).getObject()).type;
        }

        @Override
        public int getItemCount() {
            return treeDataManager.getItemCount();
        }

        protected class Holder extends BaseHolder<ItemTree> implements View.OnClickListener {

            public TextView tv;
            public Button btn;
            public BaseRecyclerView brv_datas;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
                btn = (Button) itemView.findViewById(R.id.btn);
                brv_datas = (BaseRecyclerView) itemView.findViewById(R.id.brv_datas);
            }

            private SpanResBg spanResBg = new SpanResBg(itemView.getContext(), R.drawable.shape_rectangle_corner_5dp_solid_ffffff, 100, 10);
            private SpanResBg spanResBg2 = new SpanResBg(itemView.getContext(), R.drawable.shape_rectangle_corner_5dp_solid_ffffff, 0, 0);

            @Override
            public void fillView(ItemTree data, int position) {
                super.fillView(data, position);
                RVData rvData = (RVData) data.getObject();
                SpannableString sp = new SpannableString(rvData.show);
//                new Random().nextInt(1);
                sp.setSpan(spanResBg, 0, new Random().nextInt(2) + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                sp.setSpan(spanResBg2, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(sp);
                btn.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                RVData rvData = (RVData) data.getObject();
                if (v.getId() == itemView.getId()) {
                    int position = treeDataManager.indexOf(data);
                    int itemCount = treeDataManager.flex(position);
                    if (itemCount > 0) {
                        notifyItemRangeInserted(position + 1, itemCount);
                    } else {
                        notifyItemRangeRemoved(position + 1, -itemCount);
                    }
                } else if (v.getId() == btn.getId()) {
                    Toast.makeText(v.getContext(), rvData.show, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
