package com.lyb.besttimer.androidshare.activity.processor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.java_annotation.BindClass;

@BindClass(path = "router_target")
public class RouterTargetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_target);
    }
}
