package com.lyb.besttimer.pluginwidget.view.util;

import android.view.MotionEvent;

/**
 * 触摸控制接口
 * Created by 林一彪 on 2016/7/14.
 */
public interface TouchController {

    boolean onTouchEvent(MotionEvent event);

    /**
     * 是否准备检测触摸的监听器
     */
    interface OnReadyListener {

        /**
         * 是否准备检测上拉触摸
         *
         * @param event 触摸实体
         * @return 返回是否准备标志
         */
        boolean onReadyUp(MotionEvent event);

        /**
         * 是否准备检测下拉触摸
         *
         * @param event 触摸实体
         * @return 返回是否准备标志
         */
        boolean onReadyDown(MotionEvent event);

        /**
         * 是否准备检测左拉触摸
         *
         * @param event 触摸实体
         * @return 返回是否准备标志
         */
        boolean onReadyLeft(MotionEvent event);

        /**
         * 是否准备检测右拉触摸
         *
         * @param event 触摸实体
         * @return 返回是否准备标志
         */
        boolean onReadyRight(MotionEvent event);

    }

}
