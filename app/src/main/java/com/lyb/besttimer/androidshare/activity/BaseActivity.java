package com.lyb.besttimer.androidshare.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.Constants;
import com.lyb.besttimer.pluginwidget.systembar.SystemBarTintManager;

/**
 * activity基类
 * Created by Administrator on 2016/7/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra(Constants.TITLE));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
//        tintManager.setTintColor(Color.parseColor("#999000FF"));
        // set a custom navigation bar resource
//        tintManager.setNavigationBarTintResource(R.mipmap.refresh_arrow);
        // set a custom status bar drawable
//        tintManager.setStatusBarTintResource(R.mipmap.ic_launcher);
    }

}
