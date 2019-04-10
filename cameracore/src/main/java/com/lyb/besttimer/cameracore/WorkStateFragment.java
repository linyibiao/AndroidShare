package com.lyb.besttimer.cameracore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 监听工作状态的fragment
 *
 * @author linyibiao
 * @since 2017/11/28 11:10
 */
public class WorkStateFragment extends Fragment {

    private static final String TAG = "WorkStateFragment";

    public static WorkStateFragment addToManager(FragmentManager fragmentManager) {
        WorkStateFragment httpStateFragment = (WorkStateFragment) fragmentManager.findFragmentByTag(TAG);
        if (httpStateFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(httpStateFragment = new WorkStateFragment(), TAG);
            fragmentTransaction.commit();
        }
        return httpStateFragment;
    }

    private LifeCaller lifeCaller;

    public void setLifeCaller(LifeCaller lifeCaller) {
        this.lifeCaller = lifeCaller;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (lifeCaller != null) {
            lifeCaller.onCreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lifeCaller != null) {
            lifeCaller.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lifeCaller != null) {
            lifeCaller.onPause();
        }
    }

}
