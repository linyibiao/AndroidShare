package com.lyb.besttimer.javassistgroovyapp;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lyb.besttimer.annotation_api.BindInitCenter;

public class GroovyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (isDebug()) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BindInitCenter.init(getApplicationContext());
    }

    protected boolean isDebug() {
        return true;
    }
}
