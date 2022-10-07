package pt.uninova.s4h.citizenhub;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;

public class StepsService extends Service implements SensorEventListener {

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    private static final String TAG = StepsService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        System.out.println("REACHED ONSTARTCOMMAND");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // wake screen here
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(getApplicationContext().POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        wakeLock.acquire(60*1000L /*1 minute*/);

        //and release again
        wakeLock.release();

        // grab the values and timestamp -- off the main thread
        System.out.println("REACHED ONSENSORCHANGED");
        new SensorEventLoggerTask().execute(event);
        // stop the service
        sensorManager.unregisterListener(this);
        stopSelf();
    }

    public class SensorEventLoggerTask extends
            AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            System.out.println("VALUE: " + event.values[0]);
            return null;
        }
    }


}