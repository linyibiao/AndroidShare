package com.lyb.besttimer.androidshare.activity.processor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.annotation_api.BindClassCenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_jump})
    public void onClick(View view) {
        startActivity(new Intent(this, BindClassCenter.findClass_T("router_target")));
    }

}
