package com.lyb.besttimer.pluginwidget.view.tablayout.adapter;

import android.support.design.widget.TabLayout;

import com.lyb.besttimer.pluginwidget.view.tablayout.BaseTabLayout;

/**
 * base tab adapter
 * Created by linyibiao on 2016/10/19.
 */

public abstract class BaseTabAdapter extends BaseTabLayout.Adapter<BaseTabHolder> {

    public BaseTabAdapter(TabLayout tabLayout) {
        super(tabLayout);
    }

}
