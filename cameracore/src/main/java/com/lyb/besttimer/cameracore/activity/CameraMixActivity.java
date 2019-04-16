package com.lyb.besttimer.cameracore.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyb.besttimer.cameracore.CameraConstants;
import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.R;
import com.lyb.besttimer.cameracore.databinding.ActivityCameraMixBinding;
import com.lyb.besttimer.cameracore.fragment.CameraFragment;
import com.lyb.besttimer.cameracore.fragment.CameraShowFragment;
import com.lyb.besttimer.pluginwidget.utils.FragmentUtil;
import com.lyb.besttimer.pluginwidget.view.loading.LoadingCaller;
import com.lyb.besttimer.pluginwidget.view.loading.LoadingView;

public class CameraMixActivity extends AppCompatActivity implements CameraResultCaller {

    private ActivityCameraMixBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_mix);
        showCamera();
        binding.loadvGo.setLoadingCaller(new LoadingCaller() {

            @Override
            public void takeOneShot() {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).takePicture();
                }
            }

            @Override
            public void startLoading() {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).takeRecord();
                }
            }

            @Override
            public void endLoading() {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).takeRecord();
                }
            }

            @Override
            public void moveInit() {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).moveInit();
                }
            }

            @Override
            public void moveOffset(float offsetValue) {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).moveOffset(offsetValue);
                }
            }

            @Override
            public void moveOffset(int offsetValue) {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).moveOffset(offsetValue);
                }
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.ivReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).switchCamera();
                }
            }
        });
        binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });
        binding.ivEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.loadvGo.getLoadingMode() == LoadingView.LoadingMode.IDLE) {
                    Intent data = new Intent();
                    data.putExtra(CameraConstants.fileUrl, fileUrl);
                    data.putExtra(CameraConstants.resultType, resultType);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    private void showCamera() {
        FragmentUtil.replace(getSupportFragmentManager(), R.id.layout_show, CameraFragment.class, null, null);
        binding.layoutCapture.setVisibility(View.VISIBLE);
        binding.layoutCheck.setVisibility(View.GONE);
    }

    private void showCameraPreview(String fileUrl, ResultType resultType) {
        FragmentUtil.replace(getSupportFragmentManager(), R.id.layout_show, CameraShowFragment.class, CameraShowFragment.createArg(fileUrl, resultType), null);
        binding.layoutCapture.setVisibility(View.GONE);
        binding.layoutCheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartVideo() {
        binding.loadvGo.reStartLoading();
    }

    private String fileUrl;
    private ResultType resultType;

    @Override
    public void onResult(String fileUrl, ResultType resultType) {
        this.fileUrl = fileUrl;
        this.resultType = resultType;
        showCameraPreview(fileUrl, resultType);
    }

}
