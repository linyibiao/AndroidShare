package com.lyb.besttimer.androidshare.activity.processor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.processor.Router$$Real;

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
        try {
            startActivity(new Intent(this, Class.forName(new Router$$Real().findClass("router_target"))));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
