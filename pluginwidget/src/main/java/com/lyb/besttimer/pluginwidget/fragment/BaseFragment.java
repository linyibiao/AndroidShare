package com.lyb.besttimer.pluginwidget.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * base fragment
 * Created by linyibiao on 2017/1/11.
 */

public class BaseFragment extends Fragment {

    public static <T extends BaseFragment> T newInstance(Class<T> cls, Bundle args) {
        try {
            T t = cls.newInstance();
            t.setArguments(args);
            return t;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
