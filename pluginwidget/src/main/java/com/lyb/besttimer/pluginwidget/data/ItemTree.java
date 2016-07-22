package com.lyb.besttimer.pluginwidget.data;

import java.util.ArrayList;
import java.util.List;

/**
 * element tree
 * Created by linyibiao on 2016/7/14.
 */
public class ItemTree<T> {

    /**
     * holding object
     */
    private T object;

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
    private List<ItemTree> childs = new ArrayList<>();

    public ItemTree() {
    }

    public ItemTree(T object, boolean isExpand, ItemTree father) {
        this.object = object;
        this.isExpand = isExpand;
        setFather(father);
    }

    public T getObject() {
        return this.object;
    }

    public void setObject(T object) {
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
        if (this.father != father) {
            this.father = father;
            if (father != null) {
                father.addChild(this);
            }
        }
    }

    public void addChild(ItemTree itemTree) {
        if (!this.childs.contains(itemTree)) {
            this.childs.add(itemTree);
            itemTree.setFather(this);
        }
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
    public static <T> List<ItemTree<T>> getShowTreeList(List<ItemTree<T>> trees) {
        List<ItemTree<T>> showTrees = new ArrayList<>();
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
    public static <T> List<ItemTree> getShowTreeList(ItemTree<T> tree) {
        List<ItemTree> showTrees = new ArrayList<>();
        showTrees.add(tree);
        if (tree.isExpand()) {
            for (ItemTree<T> childTree : tree.getChilds()) {
                showTrees.addAll(ItemTree.getShowTreeList(childTree));
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
    public static <T> List<ItemTree<T>> getDeepTreeList(List<ItemTree<T>> trees) {
        List<ItemTree<T>> deepTrees = new ArrayList<>();
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
    public static <T> List<ItemTree<T>> getDeepTreeList(ItemTree<T> tree) {
        List<ItemTree<T>> deepTrees = new ArrayList<>();
        deepTrees.add(tree);
        for (ItemTree<T> childTree : tree.getChilds()) {
            deepTrees.addAll(ItemTree.getDeepTreeList(childTree));
        }
        return deepTrees;
    }

    /**
     * Get a list of the same layer
     *
     * @param trees targets
     * @param layer layer,layer 0 for targets default
     * @return a list of the same layer
     */
    public static <T> List<ItemTree<T>> getLayerTreeList(List<ItemTree<T>> trees, int layer) {
        List<ItemTree<T>> layerTrees = new ArrayList<>();
        for (int i = 0; i < trees.size(); i++) {
            List<ItemTree<T>> childLayerTrees = ItemTree.getLayerTreeList(trees.get(i), layer);
            layerTrees.addAll(childLayerTrees);
        }
        return layerTrees;
    }

    /**
     * Get a list of the same layer
     *
     * @param tree  target
     * @param layer layer,layer 0 for target default
     * @return a list of the same layer
     */
    public static <T> List<ItemTree<T>> getLayerTreeList(ItemTree<T> tree, int layer) {
        List<ItemTree<T>> layerTrees = new ArrayList<>();
        if (layer > 0) {
            for (ItemTree<T> childTree : tree.getChilds()) {
                List<ItemTree<T>> childLayerTrees = ItemTree.getLayerTreeList(childTree, layer - 1);
                layerTrees.addAll(childLayerTrees);
            }
        } else
            layerTrees.add(tree);
        return layerTrees;
    }


}
