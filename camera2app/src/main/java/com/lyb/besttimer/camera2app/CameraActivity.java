package com.lyb.besttimer.camera2app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyb.besttimer.cameracore.camera2.CameraTextureView;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraTextureView cameraTextureView = findViewById(R.id.textureView);
        findViewById(R.id.btn_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraTextureView.getCameraMsgManager().takePicture();
            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraTextureView.getCameraMsgManager().takeRecord();
            }
        });
        findViewById(R.id.btn_reverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraTextureView.getCameraMsgManager().switchCamera();
            }
        });
    }
}
