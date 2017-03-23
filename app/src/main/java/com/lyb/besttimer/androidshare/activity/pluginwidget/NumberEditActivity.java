package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.view.complex.NumberEditView;

public class NumberEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_edit);
        NumberEditView what = (NumberEditView) findViewById(R.id.what);
//        what.setEnabled(false);
    }
}
