package com.lyb.besttimer.pluginwidget.view.loading;

public interface LoadingCaller {

    void takeOneShot();

    void startLoading();

    void endLoading();

    void moveInit();

    void moveOffset(float offsetValue);

    void moveOffset(int offsetValue);
}
