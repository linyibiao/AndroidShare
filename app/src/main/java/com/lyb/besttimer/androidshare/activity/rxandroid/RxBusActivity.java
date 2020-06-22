package com.lyb.besttimer.androidshare.activity.rxandroid;

import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.rxandroid.rxbus.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class RxBusActivity extends AppCompatActivity {

    private TextView tv;
    private Button btn;

    private Disposable subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);
        tv = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.btn);
        subscription = RxBus.getInstance()
                .toObserverable(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        tv.setText(s);
                    }
                });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post(SystemClock.elapsedRealtime() + "");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.dispose();
    }
}
