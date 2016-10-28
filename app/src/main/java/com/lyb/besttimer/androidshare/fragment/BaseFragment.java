package com.lyb.besttimer.androidshare.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/10/28.
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
