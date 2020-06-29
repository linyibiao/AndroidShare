package com.lyb.besttimer.androidshare.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;

import com.lyb.besttimer.pluginwidget.view.fragment.LifeCaller;
import com.lyb.besttimer.pluginwidget.view.fragment.WorkStateFragment;

public class MediaPlayerControlHelper implements LifeCaller {

    private WorkStateFragment workStateFragment;

    private HeadsetReceiver headsetReceiver = new HeadsetReceiver();

    private MediaPlayerHelper mediaPlayerHelper;
    private AudioManagerHelper audioManagerHelper;
    private ScreenControlHelper screenControlHelper;
    private SensorProximityHelper sensorProximityHelper;

    public MediaPlayerHelper getMediaPlayerHelper() {
        return mediaPlayerHelper;
    }

    public MediaPlayerControlHelper(FragmentManager fragmentManager) {
        mediaPlayerHelper = new MediaPlayerHelper();
        if (WorkStateFragment.hadAdd(fragmentManager)) {
            workStateFragment = WorkStateFragment.addToManager(fragmentManager);
            onCreate();
        } else {
            workStateFragment = WorkStateFragment.addToManager(fragmentManager);
        }
        workStateFragment.setLifeCaller(this);
    }

    @Override
    public void onCreate() {
        audioManagerHelper = new AudioManagerHelper(workStateFragment.getContext());
        screenControlHelper = new ScreenControlHelper(workStateFragment.getContext());
        sensorProximityHelper = new SensorProximityHelper(workStateFragment.getContext());
        sensorProximityHelper.onCreate(new SensorProximityHelper.StateListener() {

            private boolean operateFromSelf = false;

            @Override
            public void far() {
                if (operateFromSelf) {
                    operateFromSelf = false;
                    audioManagerHelper.changeToSpeaker();
                    screenControlHelper.setScreenOn();
                }
            }

            @Override
            public void near() {
                if (mediaPlayerHelper.isPlaying()) {
                    operateFromSelf = true;
                    audioManagerHelper.changeToReceiver();
                    screenControlHelper.setScreenOff();
                }
            }
        });
        workStateFragment.getContext().registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mediaPlayerHelper.onDestroy();
        sensorProximityHelper.onDestroy();
        workStateFragment.getContext().unregisterReceiver(headsetReceiver);
    }

    class HeadsetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                //插入和拔出耳机会触发此广播
                case Intent.ACTION_HEADSET_PLUG:
                    int state = intent.getIntExtra("state", 0);
                    if (state == 1) {
                        audioManagerHelper.changeToHeadset();
                    } else if (state == 0) {
                        audioManagerHelper.changeToSpeaker();
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
