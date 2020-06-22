package com.lyb.besttimer.cameracore.fragment;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.cameracore.R;
import com.lyb.besttimer.pluginwidget.utils.FragmentUtil;

public class CameraFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera, container, false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            FragmentUtil.replace(getChildFragmentManager(), R.id.layout_camera, CameraOldFragment.class, null, null);
        } else {
            FragmentUtil.replace(getChildFragmentManager(), R.id.layout_camera, CameraNewFragment.class, null, null);
        }
        return view;
    }

    public void takePicture() {
        Fragment fragment = FragmentUtil.findFragment(getChildFragmentManager(), R.id.layout_camera, null);
        if (fragment instanceof CameraOldFragment) {
            ((CameraOldFragment) fragment).takePicture();
        } else if (fragment instanceof CameraNewFragment) {
            ((CameraNewFragment) fragment).takePicture();
        }
    }

    public void takeRecord() {
        Fragment fragment = FragmentUtil.findFragment(getChildFragmentManager(), R.id.layout_camera, null);
        if (fragment instanceof CameraOldFragment) {
            ((CameraOldFragment) fragment).takeRecord();
        } else if (fragment instanceof CameraNewFragment) {
            ((CameraNewFragment) fragment).takeRecord();
        }
    }

    public void switchCamera() {
        Fragment fragment = FragmentUtil.findFragment(getChildFragmentManager(), R.id.layout_camera, null);
        if (fragment instanceof CameraOldFragment) {
            ((CameraOldFragment) fragment).switchCamera();
        } else if (fragment instanceof CameraNewFragment) {
            ((CameraNewFragment) fragment).switchCamera();
        }
    }

    public void moveInit() {
        Fragment fragment = FragmentUtil.findFragment(getChildFragmentManager(), R.id.layout_camera, null);
        if (fragment instanceof CameraOldFragment) {
            ((CameraOldFragment) fragment).moveInit();
        } else if (fragment instanceof CameraNewFragment) {
            ((CameraNewFragment) fragment).moveInit();
        }
    }

    public void moveOffset(float offsetValue) {
        Fragment fragment = FragmentUtil.findFragment(getChildFragmentManager(), R.id.layout_camera, null);
        if (fragment instanceof CameraNewFragment) {
            ((CameraNewFragment) fragment).moveOffset(offsetValue);
        }
    }

    public void moveOffset(int offsetValue) {
        Fragment fragment = FragmentUtil.findFragment(getChildFragmentManager(), R.id.layout_camera, null);
        if (fragment instanceof CameraOldFragment) {
            ((CameraOldFragment) fragment).moveOffset(offsetValue);
        }
    }

}
