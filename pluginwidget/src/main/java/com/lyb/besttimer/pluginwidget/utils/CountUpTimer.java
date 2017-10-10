package com.lyb.besttimer.pluginwidget.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * 计时器
 * Created by linyibiao on 2017/10/10.
 */

public abstract class CountUpTimer {

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountupInterval;

    private long mSartTime;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    public CountUpTimer(long countUpInterval) {
        mCountupInterval = countUpInterval;
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        if (!mCancelled) {
            onTick(mSartTime > 0 ? SystemClock.elapsedRealtime() - mSartTime : 0);
            onFinish(mSartTime > 0 ? SystemClock.elapsedRealtime() - mSartTime : 0);
        }
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countup.
     */
    public synchronized final CountUpTimer start() {
        mCancelled = false;
        mSartTime = SystemClock.elapsedRealtime();
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    public abstract void onTick(long millisHasPassed);

    public abstract void onFinish(long millisHasPassed);

    private static final int MSG = 1;

    // handles counting up
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountUpTimer.this) {
                if (mCancelled) {
                    return;
                }

                long lastTickStart = SystemClock.elapsedRealtime();
                onTick(lastTickStart - mSartTime);

                // take into account user's onTick taking time to execute
                long delay = lastTickStart + mCountupInterval - SystemClock.elapsedRealtime();

                // special case: user's onTick took more than interval to
                // complete, skip to next interval
                while (delay < 0) delay += mCountupInterval;

                sendMessageDelayed(obtainMessage(MSG), delay);
            }
        }
    };

}
