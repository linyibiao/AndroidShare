package com.lyb.besttimer.pluginwidget.view.pullrefresh;

import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * pull header handle
 * Created by linyibiao on 2016/8/8.
 */
public interface PullHeaderHandle {

    enum HEADERSTATE {
        NORMAL, READY, LOADING, SUCCESS, FAIL
    }

    HEADERSTATE getHeaderstate();

    void setHeaderState(HEADERSTATE headerstate);

    boolean canScrollToTop(int scrollX, int scrollY);

    int getThreshold();

    void update(int scrollX, int scrollY);

    void setImageResource(@DrawableRes int resId);

    void setStateNormalStr(String stateNormalStr);

    void setStateReadyStr(String stateReadyStr);

    void setStateLoadingStr(String stateLoadingStr);

    void setStateSuccessStr(String stateSuccessStr);

    void setStateFailStr(String stateFailStr);

    void updateMSG(String updateMSG);

    View getHeaderView();

}
