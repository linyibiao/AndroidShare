package com.lyb.besttimer.pluginwidget.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * tree data manager
 * Created by linyibiao on 2016/7/22.
 */
public class TreeDataManager {

    private List<ItemTree> itemTrees = new ArrayList<>();

    private List<ItemTree> activedTrees = new ArrayList<>();

    public TreeDataManager(List<ItemTree> itemTrees) {
        this.itemTrees = itemTrees;
        updateActivedData();
    }

    /**
     * 伸缩操作
     *
     * @param position position
     * @return 返回影响到的行数，正数表示增加，负数表示减少
     */
    public int flex(int position) {
        if (position < 0 || position >= activedTrees.size()) {
            return 0;
        }
        ItemTree targetTree = activedTrees.get(position);
        int itemCount;
        if (targetTree.isExpand()) {
            itemCount = -(ItemTree.getShowTreeList(targetTree).size() - 1);
            targetTree.setExpand(!targetTree.isExpand());
            updateActivedData();
        } else {
            targetTree.setExpand(!targetTree.isExpand());
            itemCount = ItemTree.getShowTreeList(targetTree).size() - 1;
            updateActivedData();
        }
        return itemCount;
    }

    public ItemTree getItem(int position) {
        return activedTrees.get(position);
    }

    public int getItemCount() {
        return activedTrees.size();
    }

    public int indexOf(ItemTree itemTree) {
        return activedTrees.indexOf(itemTree);
    }

    public int itemRange(int position) {
        return ItemTree.getShowTreeList(activedTrees.get(position)).size();
    }

    public void remove(int position) {
        ItemTree itemTree = activedTrees.get(position);
        if (itemTree.getFather() != null) {
            itemTree.getFather().removeChild(itemTree);
        } else {
            itemTrees.remove(itemTree);
        }
        updateActivedData();
    }

    public boolean canMove(int position1, int position2) {
        ItemTree itemTree1 = activedTrees.get(position1);
        ItemTree itemTree2 = activedTrees.get(position2);
        return itemTree1.getFather() == itemTree2.getFather();
    }

    public boolean move(int position1, int position2) {
        ItemTree itemTree1 = activedTrees.get(position1);
        ItemTree itemTree2 = activedTrees.get(position2);
        List<ItemTree> childs = null;
        if (itemTree1.getFather() == itemTree2.getFather()) {
            ItemTree father = itemTree1.getFather();
            if (father == null) {
                childs = itemTrees;
            } else {
                childs = father.getChilds();
            }
        }
        if (childs != null) {
            int index1 = childs.indexOf(itemTree1);
            int index2 = childs.indexOf(itemTree2);
            if (index1 < index2) {
                for (int position = index1; position < index2; position++) {
                    Collections.swap(childs, position, position + 1);
                }
            } else {
                for (int position = index2; position < index1; position++) {
                    Collections.swap(childs, position, position + 1);
                }
            }
            updateActivedData();
            return true;
        }
        return false;
    }

    public void updateActivedData() {
        activedTrees = ItemTree.getShowTreeList(itemTrees);
    }

    public List<ItemTree> getItemTrees() {
        return itemTrees;
    }

    public void setItemTrees(List<ItemTree> itemTrees) {
        this.itemTrees = itemTrees;
        updateActivedData();
    }

    public List<ItemTree> getActivedTrees() {
        return activedTrees;
    }

}
