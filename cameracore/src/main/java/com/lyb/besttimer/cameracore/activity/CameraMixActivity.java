package com.lyb.besttimer.cameracore.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyb.besttimer.cameracore.CameraConstants;
import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.R;
import com.lyb.besttimer.cameracore.fragment.CameraFragment;
import com.lyb.besttimer.cameracore.fragment.CameraShowFragment;
import com.lyb.besttimer.pluginwidget.utils.FragmentUtil;
import com.lyb.besttimer.pluginwidget.view.loading.LoadingCaller;
import com.lyb.besttimer.pluginwidget.view.loading.LoadingView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CameraMixActivity extends AppCompatActivity implements CameraResultCaller {

    private LoadingView loadvGo;
    private ImageView ivBack;
    private ImageView ivReverse;
    private ImageView ivCancel;
    private ImageView ivEnsure;
    private View layoutCapture;
    private View layoutCheck;

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_mix);

        loadvGo = findViewById(R.id.loadv_go);
        ivBack = findViewById(R.id.iv_back);
        ivReverse = findViewById(R.id.iv_reverse);
        ivCancel = findViewById(R.id.iv_cancel);
        ivEnsure = findViewById(R.id.iv_ensure);
        layoutCapture = findViewById(R.id.layout_capture);
        layoutCheck = findViewById(R.id.layout_check);

        rxPermissions = new RxPermissions(this);
        showCamera();
        loadvGo.setLoadingCaller(new LoadingCaller() {

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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraFragment) {
                    ((CameraFragment) fragment).switchCamera();
                }
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });
        ivEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadvGo.getLoadingMode() == LoadingView.LoadingMode.IDLE) {
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
        Disposable disposable = rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    FragmentUtil.replace(getSupportFragmentManager(), R.id.layout_show, CameraFragment.class, null, null);
                    layoutCapture.setVisibility(View.VISIBLE);
                    layoutCheck.setVisibility(View.GONE);
                } else {
                    Toast.makeText(CameraMixActivity.this, "请开启相机相关权限", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }

    private void showCameraPreview(String fileUrl, ResultType resultType) {
        FragmentUtil.replace(getSupportFragmentManager(), R.id.layout_show, CameraShowFragment.class, CameraShowFragment.createArg(fileUrl, resultType), null);
        layoutCapture.setVisibility(View.GONE);
        layoutCheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartVideo() {
        loadvGo.reStartLoading();
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
