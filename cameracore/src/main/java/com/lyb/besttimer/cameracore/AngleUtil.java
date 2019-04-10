package com.lyb.besttimer.cameracore;

import android.view.Surface;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/5/2
 * 描    述：
 * =====================================
 */
public class AngleUtil {
    public static int getSensorRotation(float x, float y) {
        if (Math.abs(x) > Math.abs(y)) {
            /*横屏倾斜角度比较大*/
            if (x > 4) {
                /*左边倾斜*/
                return Surface.ROTATION_90;
            } else if (x < -4) {
                /*右边倾斜*/
                return Surface.ROTATION_270;
            } else {
                /*倾斜角度不够大*/
                return Surface.ROTATION_0;
            }
        } else {
            if (y > 7) {
                /*不动*/
                return Surface.ROTATION_0;
            } else if (y < -7) {
                /*倒过来*/
                return Surface.ROTATION_180;
            } else {
                /*不动*/
                return Surface.ROTATION_0;
            }
        }
    }
}
