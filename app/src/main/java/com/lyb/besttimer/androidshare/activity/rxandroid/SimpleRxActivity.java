package com.lyb.besttimer.androidshare.activity.rxandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lyb.besttimer.androidshare.R;
import com.lyb.besttimer.androidshare.activity.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class SimpleRxActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_rx);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_RX, R.id.btn_merge, R.id.btn_zip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_RX:
                simpleRX();
                break;
            case R.id.btn_merge:
                mergeRX();
                break;
            case R.id.btn_zip:
                zipRX();
                break;
        }
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

    private void mergeRX() {
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("observable1");
            }
        }).delay(3000, TimeUnit.MILLISECONDS);
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("observable2");
            }
        }).delay(5000, TimeUnit.MILLISECONDS);
        Observable.merge(observable1, observable2).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("accept", "accept" + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {

            }
        });
    }

    private void zipRX() {
        Log.e("zipRX", "start");
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("observable1");
            }
        }).delay(3000, TimeUnit.MILLISECONDS);
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("observable2");
            }
        }).delay(5000, TimeUnit.MILLISECONDS);
        Observable.zip(observable1, observable2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) throws Exception {
                Log.e("zipRX", "apply");
                return s + s2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e("accept", "accept" + s);
            }
        });
    }

}
