package com.lyb.besttimer.rxandroid.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * rxbus
 * Created by linyibiao on 2017/1/16.
 */

public class RxBus {

    private static volatile RxBus rxBus;

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    private Subject bus;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T> void post(T t) {
        bus.onNext(t);
    }

}
