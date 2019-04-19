package com.lyb.besttimer.androidshare.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorProximityHelper implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    private StateListener stateListener;

    public SensorProximityHelper(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    public void onCreate(StateListener stateListener) {
        this.stateListener = stateListener;
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onDestroy() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value = event.values[0];
        if (stateListener != null) {
            if (value == sensor.getMaximumRange()) {
                stateListener.far();
            } else {
                stateListener.near();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface StateListener {
        void far();

        void near();
    }

}
