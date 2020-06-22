package com.lyb.besttimer.cameracore.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.R;
import com.lyb.besttimer.cameracore.camera2.CameraTextureView;

public class CameraNewFragment extends Fragment {

    private CameraTextureView cameraNew;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_new, container, false);
        cameraNew = view.findViewById(R.id.camera_new);
        cameraNew.registerLifeCycle(getChildFragmentManager());
        if (getActivity() instanceof CameraResultCaller) {
            cameraNew.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) getActivity());
        } else {
            Fragment parent = getParentFragment();
            while (parent != null) {
                if (parent instanceof CameraResultCaller) {
                    cameraNew.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) parent);
                    break;
                }
                parent = parent.getParentFragment();
            }
        }
        return view;
    }

    public void takePicture() {
        cameraNew.getCameraMsgManager().takePicture();
    }

    public void takeRecord() {
        cameraNew.getCameraMsgManager().takeRecord();
    }

    public void switchCamera() {
        cameraNew.getCameraMsgManager().switchCamera();
    }

    public void moveInit() {
        cameraNew.getCameraMsgManager().initZoom();
    }

    public void moveOffset(float offsetValue) {
        cameraNew.getCameraMsgManager().offsetZoom(offsetValue);
    }

}
