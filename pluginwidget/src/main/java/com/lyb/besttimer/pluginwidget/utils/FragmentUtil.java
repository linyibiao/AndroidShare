package com.lyb.besttimer.pluginwidget.utils;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.util.List;

/**
 * fragment工具类
 * containerViewId为0时说明忽略containerViewId
 * tag为null时说明忽略tag
 * Created by linyibiao on 2017/9/12.
 */

public class FragmentUtil {

    /**
     * 实例化
     *
     * @param cls  类
     * @param args 参数
     * @param <T>  继承fragment的泛型
     * @return 返回实例
     */
    public static <T extends Fragment> T newInstance(Class<T> cls, Bundle args) {
        try {
            T t = cls.newInstance();
            if (args != null) {
                t.setArguments(args);
            }
            return t;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 寻找fragment
     *
     * @param fragmentManager fragment管理器
     * @param containerViewId viewID
     * @param tag             tag
     * @return 寻找结果
     */
    public static Fragment findFragment(FragmentManager fragmentManager, @IdRes int containerViewId, @Nullable String tag) {
        Fragment preFragment;
        if (!TextUtils.isEmpty(tag)) {
            preFragment = fragmentManager.findFragmentByTag(tag);
        } else {
            preFragment = fragmentManager.findFragmentById(containerViewId);
        }
        return preFragment;
    }

    /**
     * fragment replace
     *
     * @param fragmentManager fragment管理器
     * @param containerViewId viewID
     * @param fragmentClass   fragment类
     * @param args            fragment参数
     * @param tag             tag
     */
    public static void replace(FragmentManager fragmentManager, @IdRes int containerViewId, Class<? extends Fragment> fragmentClass, Bundle args, @Nullable String tag) {
        Fragment preFragment = findFragment(fragmentManager, containerViewId, tag);
        if (preFragment != null && preFragment.getClass() == fragmentClass) {
            showAndHideOthers(fragmentManager, containerViewId, fragmentClass, args, tag);
        } else {
            try {
                fragmentManager.beginTransaction().replace(containerViewId, newInstance(fragmentClass, args), tag).commit();
            }catch (IllegalStateException ignored){

            }
        }
    }

    /**
     * fragment add
     *
     * @param fragmentManager fragment管理器
     * @param containerViewId viewID
     * @param fragmentClass   fragment类
     * @param args            fragment参数
     * @param tag             tag
     */
    public static void add(FragmentManager fragmentManager, @IdRes int containerViewId, Class<? extends Fragment> fragmentClass, Bundle args, @Nullable String tag) {
        Fragment preFragment = findFragment(fragmentManager, containerViewId, tag);
        if (!(preFragment != null && preFragment.getClass() == fragmentClass)) {
            try {
                fragmentManager.beginTransaction().add(containerViewId, newInstance(fragmentClass, args), tag).commit();
            }catch (IllegalStateException ignored){

            }
        }
    }

    /**
     * fragment show
     *
     * @param fragmentManager fragment管理器
     * @param containerViewId viewID
     * @param fragmentClass   fragment类
     * @param args            fragment参数
     * @param tag             tag
     */
    public static void show(FragmentManager fragmentManager, @IdRes int containerViewId, Class<? extends Fragment> fragmentClass, Bundle args, @Nullable String tag) {
        Fragment preFragment = findFragment(fragmentManager, containerViewId, tag);
        if (preFragment != null && preFragment.getClass() == fragmentClass) {
            try {
                fragmentManager.beginTransaction().show(preFragment).commit();
            }catch (IllegalStateException ignored){

            }
        } else {
            add(fragmentManager, containerViewId, fragmentClass, args, tag);
        }
    }

    /**
     * fragment show and hide others
     *
     * @param fragmentManager fragment管理器
     * @param containerViewId viewID
     * @param fragmentClass   fragment类
     * @param args            fragment参数
     * @param tag             tag
     */
    public static void showAndHideOthers(FragmentManager fragmentManager, @IdRes int containerViewId, Class<? extends Fragment> fragmentClass, Bundle args, @Nullable String tag) {
        show(fragmentManager, containerViewId, fragmentClass, args, tag);
        List<Fragment> childFragments = fragmentManager.getFragments();
        if (childFragments != null && childFragments.size() > 0) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (Fragment fragment : childFragments) {
                if (fragment != null && !(fragment.getClass() == fragmentClass && TextUtils.equals(fragment.getTag(), tag))) {
                    transaction.hide(fragment);
                }
            }
            try {
                transaction.commit();
            }catch (IllegalStateException ignored){

            }
        }
    }

}
