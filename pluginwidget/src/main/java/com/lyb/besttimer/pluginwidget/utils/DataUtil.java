package com.lyb.besttimer.pluginwidget.utils;

/**
 * data util
 * Created by linyibiao on 2017/3/22.
 */

public class DataUtil {

    public static int strToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
