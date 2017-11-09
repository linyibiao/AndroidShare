package com.lyb.besttimer.rxandroid.interval;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


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

    private Disposable currDisposable;

    public void startInterval() {
        stopInterval(currDisposable);
        currDisposable = startInterval(initialDelay, period, unit, scheduler);
    }

    public void stopInterval() {
        stopInterval(currDisposable);
    }

    private Disposable startInterval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        return Observable.interval(initialDelay, period, unit, scheduler).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (intervalCall != null) {
                    intervalCall.callStep(aLong);
                }
            }
        });
    }

    private void stopInterval(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public interface IntervalCall {
        void callStep(long currIndex);
    }

}
