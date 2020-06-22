package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;

import com.lyb.besttimer.androidshare.R;

import java.util.ArrayList;
import java.util.List;

public class NumberEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_edit);
        AppCompatSpinner spin = (AppCompatSpinner) findViewById(R.id.spin);
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        spin.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, integers));
    }
}
