package com.lyb.besttimer.pluginwidget.systembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * system bar tint real manager
 * Created by linyibiao on 2016/11/8.
 */

public class SystemBarTintRealManager {

    private SystemBarTintRealManager() {

    }

    private static SystemBarTintRealManager systemBarTintRealManager = new SystemBarTintRealManager();

    private Map<Activity, SystemBarTintManager> systemBarTintManagerMap = new HashMap<>();

    public static SystemBarTintManager getSystemBarTintManager(Activity activity) {
        return systemBarTintRealManager.getSystemBarTintManagerInner(activity);
    }

    private SystemBarTintManager getSystemBarTintManagerInner(Activity activity) {
        return findSystemBarTintManager(activity);
    }

    //full screen
    public static void fullScreen(Activity activity) {
        systemBarTintRealManager.fullScreenInner(activity);
    }

    private void fullScreenInner(Activity activity) {

        SystemBarTintManager systemBarTintManager = findSystemBarTintManager(activity);
        if (systemBarTintManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                }
            }
            systemBarTintManager.setStatusBarTintColor(0);
        }

    }

    public static void install(Activity activity) {
        systemBarTintRealManager.installInner(activity);
    }

    private void installInner(Activity activity) {
        if (!hasSetup(activity)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(activity, true);
                setTranslucentNavigation(activity, true);
                ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                    ViewCompat.setFitsSystemWindows(mChildView, true);
                }
            }

            // create our manager instance after the content view is set
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
            tintManager.setNavigationBarTintEnabled(true);

            systemBarTintManagerMap.put(activity, tintManager);

        }
    }

    public static void uninstall(Activity activity) {
        systemBarTintRealManager.uninstallInner(activity);
    }

    private void uninstallInner(Activity activity) {
        systemBarTintManagerMap.remove(activity);
    }

    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @TargetApi(19)
    private void setTranslucentNavigation(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private boolean hasSetup(Activity activity) {
        SystemBarTintManager systemBarTintManager = findSystemBarTintManager(activity);
        return systemBarTintManager != null;
    }

    private SystemBarTintManager findSystemBarTintManager(Activity activity) {
        for (Map.Entry<Activity, SystemBarTintManager> entry : systemBarTintManagerMap.entrySet()) {
            if (activity.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

}
