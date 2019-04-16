package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.cameracore.activity.CameraMixActivity;

public class CameraShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_show);
        startActivity(new Intent(this, CameraMixActivity.class));
    }
}
