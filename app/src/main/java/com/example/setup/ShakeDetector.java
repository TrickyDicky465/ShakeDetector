package com.example.setup;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class ShakeDetector implements SensorEventListener {

    final String TAG = "ShakeDetector";

    private static final float SHAKE_THRESHOLD = 3.5f; // m/s/2
    private static final int MIN_TIME_BETWEEN_SHAKES = 1000;

    private SensorManager sensorManager;
    private OnShakeListener shakeListener;
    private Context context;
    private long lastShake = 0;

    public interface OnShakeListener {
        public void onShake();
    }

    public ShakeDetector(Context context, OnShakeListener listener) {
        this.context = context;
        shakeListener = listener;
        resume();
    }

    public void resume() {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager == null) {
            throw new UnsupportedOperationException("Sensors not supported");
        }

        boolean supported = false;

        try {
            supported = sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            Toast.makeText(context, "Shake not Supported", Toast.LENGTH_LONG).show();
        }

        if ((!supported) && (sensorManager != null))
            sensorManager.unregisterListener(this);
    }

    public void pause() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {;}

    public void onSensorChanged(SensorEvent event) {

        //Log.e(TAG, "Sensor chsnged");

        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        long now = System.currentTimeMillis();

        long timeDifference = now - lastShake;

        if ((now - lastShake) > MIN_TIME_BETWEEN_SHAKES) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            double acceleration = Math.sqrt( Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2)) - SensorManager.GRAVITY_EARTH;

            if (acceleration > SHAKE_THRESHOLD) {

                //Log.e(TAG,"Time = " + timeDifference + " Accleration = " + acceleration + " X = " + event.values[0] + " Y = " + event.values[1] + " Z = " + event.values[2]);

                lastShake = now;
                shakeListener.onShake();
            }
        }
    }
}
