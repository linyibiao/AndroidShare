package com.lyb.besttimer.pluginwidget.view.pullrefresh;

import android.view.View;

/**
 * pull header handle
 * Created by linyibiao on 2016/8/8.
 */
public interface PullHeaderHandle {

    enum HEADERSTATE {
        NORMAL, READY, LOADING, SUCCESS
    }

    HEADERSTATE getHeaderstate();

    void setHeaderState(HEADERSTATE headerstate);

    boolean canScrollToTop(int scrollX, int scrollY);

    int getThreshold();

    void update(int scrollX, int scrollY);

    View getHeaderView();

}
