package com.lyb.besttimer.androidshare.activity.rxandroid;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.rxandroid.interval.RxInterval;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class RxIntervalActivity extends AppCompatActivity {

    private RxInterval rxInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_interval);
        Button start = findViewById(R.id.start);
        Button stop = findViewById(R.id.stop);
        final AppCompatTextView appTV = findViewById(R.id.appTV);
        rxInterval = new RxInterval(1000, 1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread(), new RxInterval.IntervalCall() {
            @Override
            public void callStep(long currIndex) {
                appTV.append(currIndex + "\n");
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxInterval.startInterval();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxInterval.stopInterval();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxInterval.stopInterval();
    }
}
