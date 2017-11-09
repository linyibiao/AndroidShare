package com.lyb.besttimer.androidshare.activity.rxandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("wtf");
            }
        });
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e("onStart", "onStart");
            }

            @Override
            public void onNext(String s) {
                Log.e("onNext", "onNext" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(observer);
    }

}
