package com.lyb.besttimer.pluginwidget.view.recyclerview;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * RecyclerView touch feature
 * Created by linyibiao on 2016/8/4.
 */
public abstract class ItemTouchFeature extends ItemTouchHelper.SimpleCallback {

    protected RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);

    public ItemTouchFeature(RecyclerView recyclerView, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        this.recyclerView = recyclerView;
    }

    public void applyFeature() {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void removeFeature() {
        itemTouchHelper.attachToRecyclerView(null);
    }

}
