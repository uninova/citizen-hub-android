package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class StepsWork extends Worker implements SensorEventListener {
    public StepsWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    SensorManager sensorManager = null;
    Sensor sensor = null;

    @NonNull
    @Override
    public Result doWork() {

        System.out.println("GOT IN THE WORKER");
        sensorManager = (SensorManager) getApplicationContext().getSystemService(getApplicationContext().SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        // wake screen here
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(getApplicationContext().POWER_SERVICE);
        String TAG = StepsWork.class.getSimpleName();
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        wakeLock.acquire(60*1000L /*1 minute*/);

        return Result.success();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // grab the values and timestamp -- off the main thread
        System.out.println("REACHED ONSENSORCHANGED");
        new SensorEventLogger().execute(sensorEvent);
        // stop the service
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class SensorEventLogger extends
            AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            System.out.println("VALUE: " + event.values[0]);
            return null;
        }
    }


}