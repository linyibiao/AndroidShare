package com.lyb.besttimer.rxandroid.life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * rx activity
 * Created by linyibiao on 2017/4/6.
 */

public class RxAppCompatActivity extends AppCompatActivity {

    public enum ActivityEvent {
        CREATE,
        RESUME,
        START,
        PAUSE,
        STOP,
        DESTORY
    }

    protected final BehaviorSubject<ActivityEvent> lifeSubject = BehaviorSubject.create();

    public <T> Observable.Transformer<T, T> bindUntilEvent(Class<T> tClass, final ActivityEvent bindEvent) {
        final Observable<ActivityEvent> observable = lifeSubject.takeFirst(new Func1<ActivityEvent, Boolean>() {
            @Override
            public Boolean call(ActivityEvent event) {
                return event.equals(bindEvent);
            }
        });

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> sourceOb) {
                return sourceOb.takeUntil(observable);
            }
        };
    }

    private Subscription bindSubscription;

    private void bindEvent() {
        if (bindSubscription != null && !bindSubscription.isUnsubscribed()) {
            unBindEvent();
        }
        bindSubscription = lifeSubject.subscribe(new Action1<ActivityEvent>() {
            @Override
            public void call(ActivityEvent activityEvent) {
                eventCall(activityEvent);
            }
        });
    }

    private void unBindEvent() {
        if (bindSubscription != null && !bindSubscription.isUnsubscribed()) {
            bindSubscription.unsubscribe();
        }
    }

    protected void eventCall(ActivityEvent bindEvent) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindEvent();
        lifeSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifeSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifeSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifeSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifeSubject.onNext(ActivityEvent.DESTORY);
        unBindEvent();
    }

}
