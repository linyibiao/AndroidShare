package com.lyb.besttimer.cameracore.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.R;
import com.lyb.besttimer.cameracore.camera1.CameraSurfaceView;

public class CameraOldFragment extends Fragment {

    private CameraSurfaceView cameraOld;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_old, container, false);
        cameraOld = view.findViewById(R.id.camera_old);
        cameraOld.registerLifeCycle(getChildFragmentManager());
        if (getActivity() instanceof CameraResultCaller) {
            cameraOld.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) getActivity());
        } else {
            Fragment parent = getParentFragment();
            while (parent != null) {
                if (parent instanceof CameraResultCaller) {
                    cameraOld.getCameraMsgManager().setCameraResultCaller((CameraResultCaller) parent);
                    break;
                }
                parent = parent.getParentFragment();
            }
        }
        return view;
    }

    public CameraSurfaceView getCameraOld() {
        return cameraOld;
    }

    public void takePicture() {
        cameraOld.getCameraMsgManager().takePicture();
    }

    public void takeRecord() {
        cameraOld.getCameraMsgManager().takeRecord();
    }

    public void switchCamera() {
        cameraOld.getCameraMsgManager().switchCamera();
    }

    public void moveInit() {
        cameraOld.getCameraMsgManager().initZoom();
    }

    public void moveOffset(int offsetValue) {
        cameraOld.getCameraMsgManager().offsetZoom(offsetValue);
    }

}
