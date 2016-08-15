package com.lyb.besttimer.androidshare.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.Constants;

/**
 * activity基类
 * Created by Administrator on 2016/7/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle(getIntent().getStringExtra(Constants.TITLE));
    }

}
