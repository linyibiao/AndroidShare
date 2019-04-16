package com.lyb.besttimer.camera1app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyb.besttimer.cameracore.camera1.CameraSurfaceView;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraSurfaceView cameraSurfaceView = findViewById(R.id.surfaceView);
        cameraSurfaceView.registerLifeCycle(getSupportFragmentManager());
        findViewById(R.id.btn_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSurfaceView.getCameraMsgManager().takePicture();
            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSurfaceView.getCameraMsgManager().takeRecord();
            }
        });
        findViewById(R.id.btn_reverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSurfaceView.getCameraMsgManager().switchCamera();
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
}
