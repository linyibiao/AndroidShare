package com.lyb.besttimer.pluginwidget.view.recyclerview.adapter;

/**
 * single adapter
 * Created by linyibiao on 2016/10/26.
 */

public abstract class BaseSingleAdapter extends BaseAdapter {

    //-1 for default what means no selection
    private int selectPosition = -1;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
