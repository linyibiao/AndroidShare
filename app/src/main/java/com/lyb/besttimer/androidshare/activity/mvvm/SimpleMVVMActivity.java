package com.lyb.besttimer.androidshare.activity.mvvm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.databinding.ActivitySimpleMvvmBinding;

public class SimpleMVVMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySimpleMvvmBinding activitySimpleMvvmBinding = DataBindingUtil.setContentView(this, R.layout.activity_simple_mvvm);
        activitySimpleMvvmBinding.tvWhatTheMvvm.setText("我在这里呀");
    }
}
