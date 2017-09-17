package com.lyb.besttimer.pluginwidget.utils;

import android.util.Log;

/**
 * log 工具类
 * Created by besttimer on 2017/9/17.
 */

public class LogUtil {

    public static void logE(String msg) {
        logE("logE", msg);
    }

    public static void logE(String tag, String msg) {
        Log.e(tag, msg);
    }

}
