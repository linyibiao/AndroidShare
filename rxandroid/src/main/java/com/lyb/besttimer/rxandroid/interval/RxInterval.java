package com.lyb.besttimer.rxandroid.interval;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;

/**
 * rx interval
 * Created by linyibiao on 2017/3/16.
 */

public class RxInterval {

    private long initialDelay;
    private long period;
    private TimeUnit unit;
    private Scheduler scheduler;

    private IntervalCall intervalCall;

    public RxInterval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler, IntervalCall intervalCall) {
        this.initialDelay = initialDelay;
        this.period = period;
        this.unit = unit;
        this.scheduler = scheduler;
        this.intervalCall = intervalCall;
    }

    private Subscription currSubscription;

    public void startInterval() {
        stopInterval(currSubscription);
        currSubscription = startInterval(initialDelay, period, unit, scheduler);
    }

    public void stopInterval() {
        stopInterval(currSubscription);
    }

    private Subscription startInterval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return Observable.interval(initialDelay, period, unit, scheduler).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                if (intervalCall != null) {
                    intervalCall.callStep(aLong);
                }
            }
        });
    }

    private void stopInterval(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public interface IntervalCall {
        void callStep(long currIndex);
    }

}
