package com.lyb.besttimer.androidshare.activity.rxandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
                subscriber.onCompleted();
            }
        });
        Observer<String> observer = new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                Toast.makeText(SimpleRxActivity.this, "what", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(SimpleRxActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        observable.map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s;
            }
        }).subscribe(observer);
        observable.flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(final String s) {
                return Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(s);
                    }
                });
            }
        }).subscribe(observer);
    }

}
