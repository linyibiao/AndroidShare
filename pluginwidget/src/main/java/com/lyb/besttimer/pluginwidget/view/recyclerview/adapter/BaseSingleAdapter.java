package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

/**
 * 单选adapter
 * Created by linyibiao on 2016/10/26.
 */

public abstract class BaseSingleAdapter extends BaseAdapter {

    //默认是-1不选择
    private int selectPosition = -1;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
