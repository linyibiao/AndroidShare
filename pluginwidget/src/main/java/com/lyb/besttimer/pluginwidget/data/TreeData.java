package com.lyb.besttimer.pluginwidget.data;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * tree data manager
 * Created by linyibiao on 2016/7/22.
 */
public class TreeData<T> {

    private RecyclerView recyclerView;

    private List<ItemTree<T>> itemTrees = new ArrayList<>();

    private List<ItemTree<T>> activedTrees = new ArrayList<>();

    public TreeData(RecyclerView recyclerView, List<ItemTree<T>> itemTrees) {
        this.recyclerView = recyclerView;
        this.itemTrees = itemTrees;
        updateActivedData();
    }

    /**
     * flex operation
     *
     * @param position position
     */
    public void flex(int position) {
        if (position < 0 || position >= activedTrees.size() || recyclerView.isAnimating()) {
            return;
        }
        ItemTree<T> targetTree = activedTrees.get(position);
        int positionStart = position + 1;
        int itemCount;
        if (targetTree.isExpand()) {
            itemCount = ItemTree.getShowTreeList(targetTree).size() - 1;
            targetTree.setExpand(!targetTree.isExpand());
            updateActivedData();
            recyclerView.getAdapter().notifyItemRangeRemoved(positionStart, itemCount);
        } else {
            targetTree.setExpand(!targetTree.isExpand());
            itemCount = ItemTree.getShowTreeList(targetTree).size() - 1;
            updateActivedData();
            recyclerView.getAdapter().notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    public ItemTree<T> getItem(int position) {
        return activedTrees.get(position);
    }

    public int getItemCount() {
        return activedTrees.size();
    }

    public int indexOf(ItemTree<T> itemTree) {
        return activedTrees.indexOf(itemTree);
    }

    private void updateActivedData() {
        activedTrees = ItemTree.getShowTreeList(itemTrees);
    }

}
