package com.lyb.besttimer.cameracore.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.R;
import com.lyb.besttimer.cameracore.databinding.CameraOldBinding;

public class CameraOldFragment extends Fragment {

    private CameraOldBinding cameraOldBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cameraOldBinding = DataBindingUtil.bind(inflater.inflate(R.layout.camera_old, container, false));
        cameraOldBinding.cameraOld.registerLifeCycle(getChildFragmentManager());
        if (getActivity() instanceof CameraResultCaller) {
            cameraOldBinding.cameraOld.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) getActivity());
        } else {
            Fragment parent = getParentFragment();
            while (parent != null) {
                if (parent instanceof CameraResultCaller) {
                    cameraOldBinding.cameraOld.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) parent);
                    break;
                }
                parent = parent.getParentFragment();
            }
        }
        return cameraOldBinding.getRoot();
    }

    public void takePicture() {
        cameraOldBinding.cameraOld.getCameraMsgManager().takePicture();
    }

    public void takeRecord() {
        cameraOldBinding.cameraOld.getCameraMsgManager().takeRecord();
    }

    public void switchCamera() {
        cameraOldBinding.cameraOld.getCameraMsgManager().switchCamera();
    }

    public void moveInit() {
        cameraOldBinding.cameraOld.getCameraMsgManager().initZoom();
    }

    public void moveOffset(int offsetValue) {
        cameraOldBinding.cameraOld.getCameraMsgManager().offsetZoom(offsetValue);
    }

}
