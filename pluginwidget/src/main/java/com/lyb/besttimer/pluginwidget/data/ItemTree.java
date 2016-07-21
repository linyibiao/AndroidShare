package com.lyb.besttimer.pluginwidget.data;

import java.util.ArrayList;
import java.util.List;

/**
 * element tree
 * <p>
 * Created by linyibiao on 2016/7/14.
 */
public class ItemTree {

    /**
     * level
     */
    private int level = 0;

    /**
     * holding object
     */
    private Object object;

    /**
     * whether to expand,true default
     */
    private boolean isExpand = true;

    /**
     * father tree
     */
    private ItemTree father = null;

    /**
     * childs it has
     */
    private List<ItemTree> childs = new ArrayList<ItemTree>();

    public ItemTree() {
    }

    public ItemTree(int level, Object object, boolean isExpand) {
        this.level = level;
        this.object = object;
        this.isExpand = isExpand;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        for (int i = 0; i < this.childs.size(); i++) {
            this.childs.get(i).setLevel(level + 1);
        }
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isExpand() {
        return this.isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public ItemTree getFather() {
        return this.father;
    }

    public void setFather(ItemTree father) {
        this.father = father;
    }

    public void addChild(ItemTree itemTree) {
        this.childs.add(itemTree);
        itemTree.setLevel(this.level + 1);
        itemTree.setFather(this);
    }

    public List<ItemTree> getChilds() {
        return this.childs;
    }


    /**
     * Get a list of shows that do not contain hidden elements.
     *
     * @param trees targets
     * @return a list of shows that do not contain hidden elements.
     */
    public static List<ItemTree> getShowTreeList(List<ItemTree> trees) {
        List<ItemTree> showTrees = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            ItemTree tree = trees.get(i);
            showTrees.addAll(ItemTree.getShowTreeList(tree));
        }
        return showTrees;
    }

    /**
     * Get a list of shows that do not contain hidden elements.
     *
     * @param tree target
     * @return a list of shows that do not contain hidden elements.
     */
    public static List<ItemTree> getShowTreeList(ItemTree tree) {
        List<ItemTree> showTrees = new ArrayList<>();
        showTrees.add(tree);
        if (tree.isExpand()) {
            for (int i = 0; i < tree.getChilds().size(); i++) {
                ItemTree child = tree.getChilds().get(i);
                showTrees.addAll(ItemTree.getShowTreeList(child));
            }
        }
        return showTrees;
    }

    /**
     * Get all lists, including hidden elements
     *
     * @param trees targets
     * @return all lists, including hidden elements
     */
    public static List<ItemTree> getDeepTreeList(List<ItemTree> trees) {
        List<ItemTree> deepTrees = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            ItemTree tree = trees.get(i);
            deepTrees.addAll(ItemTree.getDeepTreeList(tree));
        }
        return deepTrees;
    }

    /**
     * Get all lists, including hidden elements
     *
     * @param tree target
     * @return all lists, including hidden elements
     */
    public static List<ItemTree> getDeepTreeList(ItemTree tree) {
        List<ItemTree> deepTrees = new ArrayList<>();
        deepTrees.add(tree);
        for (int i = 0; i < tree.getChilds().size(); i++) {
            ItemTree child = tree.getChilds().get(i);
            deepTrees.addAll(ItemTree.getDeepTreeList(child));
        }
        return deepTrees;
    }

    /**
     * Get a list of the same level
     *
     * @param trees targets
     * @param layer layer,layer 0 for targets default
     * @return a list of the same level
     */
    public static List<ItemTree> getLayerTreeList(List<ItemTree> trees, int layer) {
        List<ItemTree> layerTrees = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            List<ItemTree> childLayerTrees = ItemTree.getLayerTreeList(trees.get(i), layer);
            layerTrees.addAll(childLayerTrees);
        }
        return layerTrees;
    }

    /**
     * Get a list of the same level
     *
     * @param tree  target
     * @param layer layer,layer 0 for target default
     * @return a list of the same level
     */
    public static List<ItemTree> getLayerTreeList(ItemTree tree, int layer) {
        List<ItemTree> layerTrees = new ArrayList<>();
        if (layer > 0) {
            for (int i = 0; i < tree.getChilds().size(); i++) {
                List<ItemTree> childLayerTrees = ItemTree.getLayerTreeList(tree.getChilds().get(i), layer - 1);
                layerTrees.addAll(childLayerTrees);
            }
        } else
            layerTrees.add(tree);
        return layerTrees;
    }


}
