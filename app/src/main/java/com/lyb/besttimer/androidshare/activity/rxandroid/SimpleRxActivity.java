package com.lyb.besttimer.androidshare.activity.rxandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class SimpleRxActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_rx);
        findViewById(R.id.btn_RX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleRX();
            }
        });
    }

    private void simpleRX() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("wtf");
//                subscriber.onCompleted();
            }
        });
        Observer<String> observer = new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                Log.e("onStart", "onStart");
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("onNext", "onNext" + s);
            }
        };
        observable.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(observer);
    }

}
