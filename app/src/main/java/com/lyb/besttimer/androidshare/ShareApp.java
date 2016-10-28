package com.lyb.besttimer.androidshare;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * share application
 * Created by linyibiao on 2016/10/27.
 */

public class ShareApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
