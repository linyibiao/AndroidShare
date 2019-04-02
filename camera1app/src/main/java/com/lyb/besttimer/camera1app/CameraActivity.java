package com.lyb.besttimer.camera1app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraSurfaceView cameraSurfaceView = findViewById(R.id.surfaceView);
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
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
