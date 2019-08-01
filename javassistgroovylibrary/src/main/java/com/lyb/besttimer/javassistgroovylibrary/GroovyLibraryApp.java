package com.lyb.besttimer.javassistgroovylibrary;

import android.app.Application;

import com.lyb.besttimer.annotation_bean.AppInitRegister;

public class GroovyLibraryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInitRegister.register(this);
    }
}
