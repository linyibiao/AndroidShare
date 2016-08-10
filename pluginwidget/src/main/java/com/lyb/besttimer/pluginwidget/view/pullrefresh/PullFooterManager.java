package com.lyb.besttimer.pluginwidget.view.pullrefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lyb.besttimer.pluginwidget.R;

/**
 * pull footer manager
 * Created by linyibiao on 2016/8/10.
 */
public class PullFooterManager {

    public PullFooterManager(Context context) {
        init(context);
    }

    private View footerView;

    private void init(Context context) {
        footerView = LayoutInflater.from(context).inflate(R.layout.pullrefresh_footer, null, false);
    }

    public View getFooterView() {
        return footerView;
    }

}
