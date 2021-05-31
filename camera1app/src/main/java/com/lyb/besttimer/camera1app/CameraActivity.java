package com.lyb.besttimer.camera1app;

import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.lyb.besttimer.cameracore.CameraResultCaller;
import com.lyb.besttimer.cameracore.fragment.CameraOldFragment;

public class CameraActivity extends AppCompatActivity implements CameraResultCaller {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_camera, new CameraOldFragment()).commit();
        findViewById(R.id.btn_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_camera);
                if (fragment instanceof CameraOldFragment) {
                    ((CameraOldFragment) fragment).takePicture();
                }
            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_camera);
                if (fragment instanceof CameraOldFragment) {
                    ((CameraOldFragment) fragment).takeRecord();
                }
            }
        });
        findViewById(R.id.btn_reverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_camera);
                if (fragment instanceof CameraOldFragment) {
                    ((CameraOldFragment) fragment).switchCamera();
                }
            }
        });
        findViewById(R.id.btn_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.btn_flash);
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_camera);
                if (fragment instanceof CameraOldFragment) {
                    CameraOldFragment cameraFragment = (CameraOldFragment) fragment;
                    String flashMode = cameraFragment.getCameraOld().getCameraMsgManager().getFlashMode();
                    if (TextUtils.equals(flashMode, Camera.Parameters.FLASH_MODE_TORCH)) {
                        cameraFragment.getCameraOld().getCameraMsgManager().controlFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        textView.setText("闪光灯关闭");
                    } else {
                        cameraFragment.getCameraOld().getCameraMsgManager().controlFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        textView.setText("闪光灯已开");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onCameraReady() {
        TextView textView = findViewById(R.id.btn_flash);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_camera);
        if (fragment instanceof CameraOldFragment) {
            CameraOldFragment cameraFragment = (CameraOldFragment) fragment;
            String flashMode = cameraFragment.getCameraOld().getCameraMsgManager().getFlashMode();
            if (TextUtils.equals(flashMode, Camera.Parameters.FLASH_MODE_TORCH)) {
                textView.setText("闪光灯已开");
            } else {
                textView.setText("闪光灯关闭");
            }
        }
    }

    @Override
    public void onStartVideo() {

    }

    @Override
    public void onResult(String fileUrl, ResultType resultType) {

    }
}
