package com.lyb.besttimer.cameracore.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyb.besttimer.cameracore.CameraConstants;
import com.lyb.besttimer.cameracore.CameraMode;
import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.R;
import com.lyb.besttimer.cameracore.fragment.CameraOldFragment;
import com.lyb.besttimer.cameracore.fragment.CameraShowFragment;
import com.lyb.besttimer.pluginwidget.utils.FragmentUtil;
import com.lyb.besttimer.pluginwidget.view.loading.LoadingCaller;
import com.lyb.besttimer.pluginwidget.view.loading.LoadingView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CameraMixActivity extends AppCompatActivity implements CameraResultCaller {

    public static Bundle getBundle(long millisforDuration, CameraMode cameraMode) {
        Bundle bundle = new Bundle();
        bundle.putLong(CameraConstants.millisInFuture, millisforDuration);
        bundle.putSerializable(CameraConstants.cameraMode, cameraMode);
        return bundle;
    }

    private LoadingView loadvGo;
    private ImageView ivBack;
    private ImageView ivReverse;
    private ImageView ivCancel;
    private ImageView ivEnsure;
    private View layoutCapture;
    private View layoutCheck;

    private RxPermissions rxPermissions;

    private CameraMode cameraMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_mix);

        long millisInFuture = getIntent().getLongExtra(CameraConstants.millisInFuture, 10 * 1000);
        if (getIntent().hasExtra(CameraConstants.cameraMode)) {
            cameraMode = (CameraMode) getIntent().getSerializableExtra(CameraConstants.cameraMode);
        } else {
            cameraMode = CameraMode.ALL;
        }

        loadvGo = findViewById(R.id.loadv_go);
        ivBack = findViewById(R.id.iv_back);
        ivReverse = findViewById(R.id.iv_reverse);
        ivCancel = findViewById(R.id.iv_cancel);
        ivEnsure = findViewById(R.id.iv_ensure);
        layoutCapture = findViewById(R.id.layout_capture);
        layoutCheck = findViewById(R.id.layout_check);

        loadvGo.setMillisInFuture(millisInFuture);
        if (!(cameraMode == CameraMode.ALL || cameraMode == CameraMode.VIDEO)) {
            loadvGo.setCanLoad(false);
        }

        rxPermissions = new RxPermissions(this);
        showCamera();
        loadvGo.setLoadingCaller(new LoadingCaller() {

            @Override
            public void takeOneShot() {
                if (cameraMode == CameraMode.ALL || cameraMode == CameraMode.PICTURE) {
                    Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                    if (fragment instanceof CameraOldFragment) {
                        ((CameraOldFragment) fragment).takePicture();
                    }
                }
            }

            @Override
            public void startLoading() {
                if (cameraMode == CameraMode.ALL || cameraMode == CameraMode.VIDEO) {
                    Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                    if (fragment instanceof CameraOldFragment) {
                        ((CameraOldFragment) fragment).takeRecord();
                    }
                }
            }

            @Override
            public void endLoading() {
                if (cameraMode == CameraMode.ALL || cameraMode == CameraMode.VIDEO) {
                    Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                    if (fragment instanceof CameraOldFragment) {
                        ((CameraOldFragment) fragment).takeRecord();
                    }
                }
            }

            @Override
            public void moveInit() {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraOldFragment) {
                    ((CameraOldFragment) fragment).moveInit();
                }
            }

            @Override
            public void moveOffset(int offsetValue) {
                Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
                if (fragment instanceof CameraOldFragment) {
                    ((CameraOldFragment) fragment).moveOffset(offsetValue);
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
                if (fragment instanceof CameraOldFragment) {
                    ((CameraOldFragment) fragment).switchCamera();
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
        Observable<Boolean> observable;
        if (cameraMode == CameraMode.PICTURE) {
            observable = rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            observable = rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
        }
        Disposable disposable = observable.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    FragmentUtil.replace(getSupportFragmentManager(), R.id.layout_show, CameraOldFragment.class, null, null);
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
    public void onCameraReady() {
        Fragment fragment = FragmentUtil.findFragment(getSupportFragmentManager(), R.id.layout_show, null);
        if (fragment instanceof CameraOldFragment) {
            ((CameraOldFragment) fragment).getCameraOld().getCameraMsgManager().controlSensor(false);
        }
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
