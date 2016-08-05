package com.lyb.besttimer.pluginwidget.view.recycleview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * RecyclerView touch feature
 * Created by linyibiao on 2016/8/4.
 */
public abstract class ItemTouchFeature extends ItemTouchHelper.SimpleCallback {

    protected RecyclerView recyclerView;

    public ItemTouchFeature(RecyclerView recyclerView, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        this.recyclerView = recyclerView;
    }

    public void applyFeature() {
        new ItemTouchHelper(this).attachToRecyclerView(recyclerView);
    }

}
