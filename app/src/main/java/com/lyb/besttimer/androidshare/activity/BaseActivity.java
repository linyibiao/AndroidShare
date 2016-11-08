package com.lyb.besttimer.androidshare.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lyb.besttimer.androidshare.Constants;
import com.lyb.besttimer.pluginwidget.systembar.SystemBarTintRealManager;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * activity基类
 * Created by Administrator on 2016/7/15.
 */
public class BaseActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(Constants.TITLE));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SystemBarTintRealManager.install(this);
        SystemBarTintRealManager.getSystemBarTintManager(this).setStatusBarTintColor(0xff3F51B5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SystemBarTintRealManager.uninstall(this);
    }
}
