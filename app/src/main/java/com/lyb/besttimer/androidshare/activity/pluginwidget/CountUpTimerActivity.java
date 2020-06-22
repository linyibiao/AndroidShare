package com.lyb.besttimer.androidshare.activity.pluginwidget;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.pluginwidget.utils.CountUpTimer;

public class CountUpTimerActivity extends AppCompatActivity {

    private TextView tv_timer;
    private TextView tv_final_timer;

    private CountUpTimer countUpTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_up_timer);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_final_timer = (TextView) findViewById(R.id.tv_final_timer);
        countUpTimer = new CountUpTimer(1000) {
            @Override
            public void onTick(long millisHasPassed) {
                tv_timer.setText(millisHasPassed / 1000 + "秒");
            }

            @Override
            public void onFinish(long millisHasPassed) {
                tv_final_timer.setText(millisHasPassed / 1000 + "秒");

            }
        };
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUpTimer.start();
            }
        });
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUpTimer.cancel();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countUpTimer.cancel();
    }
}
