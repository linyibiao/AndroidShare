package com.lyb.besttimer.rxandroid.life;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;


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

    public <T> ObservableTransformer<T, T> bindUntilEvent(Class<T> tClass, final ActivityEvent bindEvent) {

        final Observable<ActivityEvent> observable = lifeSubject.filter(new Predicate<ActivityEvent>() {
            @Override
            public boolean test(ActivityEvent event) throws Exception {
                return event.equals(bindEvent);
            }
        });

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.takeUntil(observable);
            }
        };
    }

    private Disposable bindSubscription;

    private void bindEvent() {
        if (bindSubscription != null && !bindSubscription.isDisposed()) {
            unBindEvent();
        }
        bindSubscription = lifeSubject.subscribe(new Consumer<ActivityEvent>() {
            @Override
            public void accept(ActivityEvent activityEvent) throws Exception {
                eventCall(activityEvent);
            }
        });
    }

    private void unBindEvent() {
        if (bindSubscription != null && !bindSubscription.isDisposed()) {
            bindSubscription.dispose();
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
