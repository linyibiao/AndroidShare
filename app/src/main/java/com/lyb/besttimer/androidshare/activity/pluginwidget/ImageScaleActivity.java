package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.framelayout.FrameLayoutScale;

public class ImageScaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scale);
        final FrameLayoutScale scale = findViewById(R.id.scale);
        scale.postDelayed(new Runnable() {
            @Override
            public void run() {
                scale.setHwfactor(1);
            }
        }, 5000);
    }
}
