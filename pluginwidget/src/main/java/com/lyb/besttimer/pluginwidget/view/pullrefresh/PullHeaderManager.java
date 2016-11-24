package com.lyb.besttimer.pluginwidget.view.pullrefresh;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyb.besttimer.pluginwidget.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * pull header view
 * Created by linyibiao on 2016/8/8.
 */
public class PullHeaderManager implements PullHeaderHandle {

    public PullHeaderManager(Context context) {
        this(context, R.layout.pullrefresh_header);
    }

    public PullHeaderManager(Context context, @LayoutRes int layoutID) {
        init(context, layoutID);
    }

    private View headerView;

    private View layout_header;
    private RelativeLayout rl_refresh_pic;
    private ImageView iv_arrow;
    private ProgressBar pb_loading;
    private LinearLayout ll_state_show;
    private TextView tv_state;
    private TextView tv_updateMSG;
    private TextView tv_success;
    private TextView tv_fail;

    private void init(Context context, @LayoutRes int layoutID) {
        headerView = LayoutInflater.from(context).inflate(layoutID, null, false);
        layout_header = headerView.findViewById(R.id.layout_header);
        rl_refresh_pic = (RelativeLayout) headerView.findViewById(R.id.rl_refresh_pic);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_loading = (ProgressBar) headerView.findViewById(R.id.pb_loading);
        ll_state_show = (LinearLayout) headerView.findViewById(R.id.ll_state_show);
        tv_state = (TextView) headerView.findViewById(R.id.tv_state);
        tv_updateMSG = (TextView) headerView.findViewById(R.id.tv_updateMSG);
        tv_success = (TextView) headerView.findViewById(R.id.tv_success);
        tv_fail = (TextView) headerView.findViewById(R.id.tv_fail);

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
                setCompleted(true);
                break;
            case FAIL:
                setCompleted(false);
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
            case FAIL:
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
            case FAIL:
                if (scrollY >= 0) {
                    setHeaderState(HEADERSTATE.NORMAL);
                }
                break;
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        iv_arrow.setImageResource(resId);
    }

    private String stateNormalStr = "pull refresh";
    private String stateReadyStr = "release to refresh";
    private String stateLoadingStr = "refreshing...";
    private String stateSuccessStr = "refresh complete";
    private String stateFailStr = "refresh fail";

    @Override
    public void setStateNormalStr(String stateNormalStr) {
        this.stateNormalStr = stateNormalStr;
        if (headerstate == HEADERSTATE.NORMAL) {
            tv_state.setText(stateNormalStr);
        }
    }

    @Override
    public void setStateReadyStr(String stateReadyStr) {
        this.stateReadyStr = stateReadyStr;
        if (headerstate == HEADERSTATE.READY) {
            tv_state.setText(stateReadyStr);
        }
    }

    @Override
    public void setStateLoadingStr(String stateLoadingStr) {
        this.stateLoadingStr = stateLoadingStr;
        if (headerstate == HEADERSTATE.LOADING) {
            tv_state.setText(stateLoadingStr);
        }
    }

    @Override
    public void setStateSuccessStr(String stateSuccessStr) {
        this.stateSuccessStr = stateSuccessStr;
        if (headerstate == HEADERSTATE.SUCCESS) {
            tv_success.setText(stateSuccessStr);
        }
    }

    @Override
    public void setStateFailStr(String stateFailStr) {
        this.stateFailStr = stateFailStr;
        if (headerstate == HEADERSTATE.FAIL) {
            tv_fail.setText(stateFailStr);
        }
    }

    @Override
    public void updateMSG(String updateMSG) {
        tv_updateMSG.setText(updateMSG);
        if (TextUtils.isEmpty(updateMSG)) {
            tv_updateMSG.setVisibility(View.GONE);
        } else {
            tv_updateMSG.setVisibility(View.VISIBLE);
        }
    }

    private static final long mDuration = 200;

    private void rotateArrow(int finalRotation) {
        long duration = (long) (Math.abs((ViewHelper.getRotation(iv_arrow) - finalRotation)) / 360f * mDuration);
        ObjectAnimator.ofFloat(iv_arrow, "rotation", ViewHelper.getRotation(iv_arrow), finalRotation).setDuration(duration).start();
    }

    private void setNormal() {

        rl_refresh_pic.setVisibility(View.VISIBLE);
        iv_arrow.setVisibility(View.VISIBLE);
        rotateArrow(0);
        pb_loading.setVisibility(View.INVISIBLE);

        ll_state_show.setVisibility(View.VISIBLE);
        tv_state.setText(stateNormalStr);

        tv_success.setVisibility(View.INVISIBLE);
        tv_fail.setVisibility(View.INVISIBLE);

    }

    private void setReady() {

        rl_refresh_pic.setVisibility(View.VISIBLE);
        iv_arrow.setVisibility(View.VISIBLE);
        rotateArrow(180);
        pb_loading.setVisibility(View.INVISIBLE);

        ll_state_show.setVisibility(View.VISIBLE);
        tv_state.setText(stateReadyStr);

        tv_success.setVisibility(View.INVISIBLE);
        tv_fail.setVisibility(View.INVISIBLE);

    }

    private void setLoading() {

        rl_refresh_pic.setVisibility(View.VISIBLE);
        iv_arrow.setVisibility(View.INVISIBLE);
        pb_loading.setVisibility(View.VISIBLE);

        ll_state_show.setVisibility(View.VISIBLE);
        tv_state.setText(stateLoadingStr);

        tv_success.setVisibility(View.INVISIBLE);
        tv_fail.setVisibility(View.INVISIBLE);

    }

    private void setCompleted(boolean successful) {

        rl_refresh_pic.setVisibility(View.INVISIBLE);

        ll_state_show.setVisibility(View.INVISIBLE);

        if (successful) {
            tv_success.setVisibility(View.VISIBLE);
            tv_success.setText(stateSuccessStr);
            tv_fail.setVisibility(View.INVISIBLE);
        } else {
            tv_fail.setVisibility(View.VISIBLE);
            tv_fail.setText(stateFailStr);
            tv_success.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public View getHeaderView() {
        return headerView;
    }
}
