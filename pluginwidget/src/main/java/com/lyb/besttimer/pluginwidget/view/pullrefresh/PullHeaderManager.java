package com.lyb.besttimer.pluginwidget.view.pullrefresh;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyb.besttimer.pluginwidget.R;

/**
 * pull header view
 * Created by linyibiao on 2016/8/8.
 */
public class PullHeaderManager implements PullHeaderHandle {

    public PullHeaderManager(Context context) {
        init(context);
    }

    private View headerView;

    private View layout_header;
    private RelativeLayout rl_refresh_pic;
    private ImageView iv_arrow;
    private ProgressBar pb_loading;
    private LinearLayout ll_state_show;
    private TextView tv_state;
    private TextView tv_time;
    private TextView tv_success;

    private void init(Context context) {
        headerView = LayoutInflater.from(context).inflate(R.layout.pullrefresh_header, null, false);
        layout_header = headerView.findViewById(R.id.layout_header);
        rl_refresh_pic = (RelativeLayout) headerView.findViewById(R.id.rl_refresh_pic);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_loading = (ProgressBar) headerView.findViewById(R.id.pb_loading);
        ll_state_show = (LinearLayout) headerView.findViewById(R.id.ll_state_show);
        tv_state = (TextView) headerView.findViewById(R.id.tv_state);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        tv_success = (TextView) headerView.findViewById(R.id.tv_success);

        setHeaderState(HEADERSTATE.NORMAL);

    }

    private HEADERSTATE headerstate;

    @Override
    public HEADERSTATE getHeaderstate() {
        return headerstate;
    }

    @Override
    public void setHeaderState(HEADERSTATE headerstate) {
        if (this.headerstate == headerstate) {
            return;
        }
        switch (headerstate) {
            case NORMAL:
                setNormal();
                break;
            case READY:
                setReady();
                break;
            case LOADING:
                setLoading();
                break;
            case SUCCESS:
                setSuccess();
                break;
        }
        this.headerstate = headerstate;
    }

    @Override
    public boolean canScrollToTop(int scrollX, int scrollY) {
        boolean canScrollToTop = true;
        switch (headerstate) {
            case NORMAL:
                break;
            case READY:
                canScrollToTop = false;
                break;
            case LOADING:
                if (scrollY <= -getThreshold()) {
                    canScrollToTop = false;
                }
                break;
            case SUCCESS:
                break;
        }
        return canScrollToTop;
    }

    public int getThreshold() {
        return layout_header.getHeight();
    }

    @Override
    public void update(int scrollX, int scrollY) {
        switch (headerstate) {
            case NORMAL:
            case READY:
                if (scrollY > -getThreshold()) {
                    setHeaderState(HEADERSTATE.NORMAL);
                } else if (scrollY < -getThreshold()) {
                    setHeaderState(HEADERSTATE.READY);
                }
                break;
            case LOADING:
                break;
            case SUCCESS:
                if (scrollY >= 0) {
                    setHeaderState(HEADERSTATE.NORMAL);
                }
                break;
        }
    }

    private final String tv_state_normal = "pull refresh";
    private final String tv_state_ready = "release to refresh";
    private final String tv_state_loading = "refreshing...";

    private void setNormal() {

        rl_refresh_pic.setVisibility(View.VISIBLE);
        iv_arrow.setVisibility(View.VISIBLE);
        ViewCompat.setRotation(iv_arrow, 0);
        pb_loading.setVisibility(View.INVISIBLE);

        ll_state_show.setVisibility(View.VISIBLE);
        tv_state.setText(tv_state_normal);

        tv_success.setVisibility(View.INVISIBLE);

    }

    private void setReady() {

        rl_refresh_pic.setVisibility(View.VISIBLE);
        iv_arrow.setVisibility(View.VISIBLE);
        ViewCompat.setRotation(iv_arrow, 180);
        pb_loading.setVisibility(View.INVISIBLE);

        ll_state_show.setVisibility(View.VISIBLE);
        tv_state.setText(tv_state_ready);

        tv_success.setVisibility(View.INVISIBLE);

    }

    private void setLoading() {

        rl_refresh_pic.setVisibility(View.VISIBLE);
        iv_arrow.setVisibility(View.INVISIBLE);
        pb_loading.setVisibility(View.VISIBLE);

        ll_state_show.setVisibility(View.VISIBLE);
        tv_state.setText(tv_state_loading);

        tv_success.setVisibility(View.INVISIBLE);

    }

    private void setSuccess() {

        rl_refresh_pic.setVisibility(View.INVISIBLE);

        ll_state_show.setVisibility(View.INVISIBLE);

        tv_success.setVisibility(View.VISIBLE);

    }

    @Override
    public View getHeaderView() {
        return headerView;
    }
}
