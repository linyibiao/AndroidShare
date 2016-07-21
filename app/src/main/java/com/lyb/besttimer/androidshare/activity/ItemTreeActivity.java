package com.lyb.besttimer.androidshare.activity;

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
import com.lyb.besttimer.pluginwidget.view.recycleview.HeaderFeature;

import java.util.ArrayList;
import java.util.List;

public class ItemTreeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_tree);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<RVDate> rvDates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RVDate rvDate0 = new RVDate("层次0" + i, 0);
            rvDates.add(rvDate0);
            for (int j = 0; j < 3; j++) {
                RVDate rvDate1 = new RVDate("层次1" + j, 1);
                rvDate0.rvDates.add(rvDate1);
                for (int k = 0; k < 3; k++) {
                    RVDate rvDate2 = new RVDate("层次2" + k, 2);
                    rvDate1.rvDates.add(rvDate2);
                }
            }
        }
        recyclerView.setAdapter(new MyAdapter(rvDates));

        new HeaderFeature(recyclerView, findViewById(R.id.rv_header), HeaderFeature.HEADER_ORIENTION.VERTICAL) {

            @Override
            public boolean isHeader(RecyclerView recyclerView, int position) {
                int type = recyclerView.getAdapter().getItemViewType(position);
                return type == 1;
            }
        }.applyFeature();

    }

    private static class RVDate {

        public List<RVDate> rvDates = new ArrayList<>();

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
            View view = null;
            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_level0, parent, false);
                    break;
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_level1, parent, false);
                    break;
                case 2:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_level2, parent, false);
                    break;
            }
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final RVDate rvDate = rvDates.get(position);
            TextView textView = holder.tv;
            textView.setText(rvDate.show);
            Button btn = holder.btn;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), rvDate.show, Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeOperation(rvDate);
                }
            });
        }

        private void changeOperation(RVDate rvDate) {
            int position = rvDates.indexOf(rvDate);
            if (rvDate.rvDates.size() > 0) {
                RVDate endRvDate = rvDate.rvDates.get(rvDate.rvDates.size() - 1);
                if (rvDates.size() > position + 1 && rvDate.rvDates.contains(rvDates.get(position + 1))) {
                    int endCondition = position + rvDate.rvDates.indexOf(endRvDate) + 1;
                    int startCondition = position + 1;
                    for (int index = endCondition; index >= startCondition; index--) {
                        rvDates.remove(index);
                        notifyItemRemoved(index);
                    }
                } else if (rvDates.size() <= position + 1 || !rvDate.rvDates.contains(rvDates.get(position + 1))) {
                    int startCondition = position + 1;
                    int endCondition = position + rvDate.rvDates.size();
                    for (int index = startCondition; index <= endCondition; index++) {
                        rvDates.add(index, rvDate.rvDates.get(index - position - 1));
                        notifyItemInserted(index);
                    }
                }
            }
//            itemTree.setExpand(!itemTree.isExpand());
//            int changeCount = 0;
//            for (ItemTree childTree : itemTree.getChilds()) {
//                List<ItemTree> showTrees = ItemTree.getShowTreeList(childTree);
//                changeCount += showTrees.size();
//            }
//            if (itemTree.isExpand()) {
//                for (int insertPosition = position + 1; insertPosition <= position + 1 + changeCount; insertPosition++) {
//                    notifyItemInserted(insertPosition);
//                }
////                notifyItemRangeInserted(position + 1, changeCount);
//            } else {
//                for (int removePosition = position + 1 + changeCount; removePosition >= position + 1; removePosition--) {
//                    notifyItemRemoved(removePosition);
//                }
////                notifyItemRangeRemoved(position + 1, changeCount);
//            }
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
