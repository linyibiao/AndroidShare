package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;
import com.lyb.besttimer.pluginwidget.data.ItemTree;
import com.lyb.besttimer.pluginwidget.data.TreeDataManager;
import com.lyb.besttimer.pluginwidget.view.pullrefresh.PullRefreshView;
import com.lyb.besttimer.pluginwidget.view.recyclerview.HeaderFeature;
import com.lyb.besttimer.pluginwidget.view.recyclerview.ItemTouchFeature;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseAdapter;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.BaseHolder;
import com.lyb.besttimer.pluginwidget.view.recyclerview.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PullRefreshActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Random random = new Random(System.currentTimeMillis());

        List<ItemTree> itemTrees = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            ItemTree itemTree0 = new ItemTree(new RVData("层次" + i, 0), true, null);
            itemTrees.add(itemTree0);
            for (int j = 0; j < 1 /*+ random.nextInt(3)*/; j++) {
                ItemTree itemTree1 = new ItemTree(new RVData("层次" + i + j, 1), true, itemTree0);
                for (int k = 0; k < 1 /*+ random.nextInt(3)*/; k++) {
                    new ItemTree(new RVData("层次" + i + j + k, 2), true, itemTree1);
                }
            }
        }
        final LoadMoreAdapter<MyAdapter> loadMoreAdapter = new LoadMoreAdapter<>(new MyAdapter(new TreeDataManager(recyclerView, itemTrees)), new LoadMoreAdapter.MoreListener() {

            private Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    LoadMoreAdapter loadMoreAdapter = (LoadMoreAdapter) recyclerView.getAdapter();
                    loadMoreAdapter.loadMoreCompleted(true, true);
                    Log.e("what", "whatwhat_inside");
                }
            };

            @Override
            public void onLoadMore() {
                Log.e("what", "whatwhat_outside");
                recyclerView.postDelayed(runnable, 5000);
            }

            @Override
            public void interruptLoad() {
                recyclerView.removeCallbacks(runnable);
            }

            @Override
            public void onLoadCompleted(boolean successful) {
                Toast.makeText(recyclerView.getContext(), "加载完毕" + successful, Toast.LENGTH_SHORT).show();
            }
        });
        loadMoreAdapter.updateMoreData(new LoadMoreAdapter.MoreData("加载更多", "加载失败", "没有更多了"));
        recyclerView.setAdapter(loadMoreAdapter);

        final PullRefreshView pullRefreshView = (PullRefreshView) findViewById(R.id.prv);

        pullRefreshView.setPullListener(new PullRefreshView.PullListener() {
            @Override
            public void onRefresh() {
                pullRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefreshView.refreshCompleted(true);
                    }
                }, 5000);
            }

            @Override
            public void onRefreshCompleted(boolean successful) {
                loadMoreAdapter.notifyDataSetChanged();
                Toast.makeText(PullRefreshActivity.this, "刷新完毕" + successful, Toast.LENGTH_SHORT).show();
            }
        });

        pullRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefreshView.forceToRefresh();
            }
        }, 5000);

        new ItemTouchFeature(recyclerView, ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {//移动
                MyAdapter myAdapter = ((LoadMoreAdapter<MyAdapter>) recyclerView.getAdapter()).getmWrapperAdapter();
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (myAdapter.getTreeDataManager().canMove(fromPosition, toPosition)) {
                    int fromRangeCount = myAdapter.getTreeDataManager().itemRange(fromPosition);
                    int toRangeCount = myAdapter.getTreeDataManager().itemRange(toPosition);
                    myAdapter.getTreeDataManager().move(fromPosition, toPosition);
                    //注意移动顺序
                    if (fromPosition < toPosition) {
                        for (int moveValue = fromRangeCount - 1; moveValue >= 0; moveValue--) {
                            recyclerView.getAdapter().notifyItemMoved(fromPosition + moveValue, toPosition + toRangeCount - 1 - (fromRangeCount - 1 - moveValue));
                        }
                    } else {
                        for (int moveValue = 0; moveValue <= fromRangeCount - 1; moveValue++) {
                            recyclerView.getAdapter().notifyItemMoved(fromPosition + moveValue, toPosition + moveValue);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {//删除
                MyAdapter myAdapter = ((LoadMoreAdapter<MyAdapter>) recyclerView.getAdapter()).getmWrapperAdapter();
                int position = viewHolder.getAdapterPosition();
                int rangeCount = myAdapter.getTreeDataManager().itemRange(position);
                myAdapter.getTreeDataManager().remove(position);
                recyclerView.getAdapter().notifyItemRangeRemoved(position, rangeCount);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {//获取删除的操作方向符，如果为0则没有删除操作响应
                MyAdapter myAdapter = ((LoadMoreAdapter<MyAdapter>) recyclerView.getAdapter()).getmWrapperAdapter();
                RVData rvData = (RVData) myAdapter.getTreeDataManager().getItem(viewHolder.getAdapterPosition()).getObject();
                if (rvData.type != 1) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {//获取拖拽的操作方向符，如果为0则没有拖拽操作响应
                MyAdapter myAdapter = ((LoadMoreAdapter<MyAdapter>) recyclerView.getAdapter()).getmWrapperAdapter();
                RVData rvData = (RVData) myAdapter.getTreeDataManager().getItem(viewHolder.getAdapterPosition()).getObject();
                if (rvData.type != 1) {
                    return 0;
                }
                return super.getDragDirs(recyclerView, viewHolder);
            }

            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {//判断拖拽过程中，target是否接受做移动处理，默认是true
                return super.canDropOver(recyclerView, current, target);
            }
        }.applyFeature();

        new HeaderFeature(recyclerView, (FrameLayout) findViewById(R.id.rv_header), HeaderFeature.HEADER_ORIENTION.VERTICAL) {

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

    private static class MyAdapter extends BaseAdapter<MyAdapter.Holder> {

        private TreeDataManager treeDataManager;

        public MyAdapter(TreeDataManager treeDataManager) {
            this.treeDataManager = treeDataManager;
        }

        public TreeDataManager getTreeDataManager() {
            return treeDataManager;
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
        public void onBindViewHolder(Holder holder, final int position) {
            final ItemTree itemTree = treeDataManager.getItem(position);
            final RVData rvData = (RVData) itemTree.getObject();
            holder.fillView(rvData, position);
        }

        @Override
        public int getItemViewType(int position) {
            return ((RVData) treeDataManager.getItem(position).getObject()).type;
        }

        @Override
        public int getItemCount() {
            return treeDataManager.getItemCount();
        }

        protected class Holder extends BaseHolder<RVData> {

            public TextView tv;
            public Button btn;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
                btn = (Button) itemView.findViewById(R.id.btn);
            }

            @Override
            public void fillView(final RVData data, int position) {
                final ItemTree itemTree = treeDataManager.getItem(position);
                TextView textView = tv;
                textView.setText(data.show);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), data.show, Toast.LENGTH_SHORT).show();
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        treeDataManager.flex(treeDataManager.indexOf(itemTree));
                    }
                });
            }
        }

    }
}
