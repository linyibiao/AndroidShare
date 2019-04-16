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
import com.lyb.besttimer.cameracore.databinding.CameraNewBinding;

public class CameraNewFragment extends Fragment {

    private CameraNewBinding cameraNewBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cameraNewBinding = DataBindingUtil.bind(inflater.inflate(R.layout.camera_new, container, false));
        cameraNewBinding.cameraNew.registerLifeCycle(getChildFragmentManager());
        if (getActivity() instanceof CameraResultCaller) {
            cameraNewBinding.cameraNew.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) getActivity());
        } else {
            Fragment parent = getParentFragment();
            while (parent != null) {
                if (parent instanceof CameraResultCaller) {
                    cameraNewBinding.cameraNew.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) parent);
                    break;
                }
                parent = parent.getParentFragment();
            }
        }
        return cameraNewBinding.getRoot();
    }

    public void takePicture() {
        cameraNewBinding.cameraNew.getCameraMsgManager().takePicture();
    }

    public void takeRecord() {
        cameraNewBinding.cameraNew.getCameraMsgManager().takeRecord();
    }

    public void switchCamera() {
        cameraNewBinding.cameraNew.getCameraMsgManager().switchCamera();
    }

    public void moveInit() {
        cameraNewBinding.cameraNew.getCameraMsgManager().initZoom();
    }

    public void moveOffset(float offsetValue) {
        cameraNewBinding.cameraNew.getCameraMsgManager().offsetZoom(offsetValue);
    }

}
