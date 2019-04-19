package com.lyb.besttimer.androidshare.utils;

import android.content.Context;
import android.os.PowerManager;

public class ScreenControlHelper {

    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    public ScreenControlHelper(Context context) {
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public void setScreenOff() {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, ScreenControlHelper.class.toString());
        }
        wakeLock.acquire();
    }

    public void setScreenOn() {
        if (wakeLock != null) {
            wakeLock.setReferenceCounted(false);
            wakeLock.release();
            wakeLock = null;
        }
    }

}
