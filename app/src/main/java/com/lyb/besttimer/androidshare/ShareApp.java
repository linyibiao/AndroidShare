package com.lyb.besttimer.androidshare;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lyb.besttimer.x5webcore.X5Init;
import com.squareup.leakcanary.LeakCanary;

/**
 * share application
 * Created by linyibiao on 2016/10/27.
 */

public class ShareApp extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

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

        X5Init.init(this);

    }
}
