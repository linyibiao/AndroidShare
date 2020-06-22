package com.lyb.besttimer.androidshare.activity.monitorCheck;

import android.os.Bundle;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.lyb.besttimer.androidshare.R;

public class MemoryCheckActivity extends AppCompatActivity {

//    Looper looper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momery_check);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //会持有外部对象哦，需要退出Looper循环
                Looper.prepare();
//                looper=Looper.myLooper();
                Looper.loop();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        looper.quit();
    }
}
