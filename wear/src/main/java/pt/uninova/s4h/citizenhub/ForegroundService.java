package pt.uninova.s4h.citizenhub;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    private SensorEventListener sensorListener = null;


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.img_logo_figure)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        System.out.println("I'm here");

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
                    System.out.println("Sensor Event from foreground service: " + event.values[0]);
                    //stopSelf();
                }
                if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                    System.out.println("Sensor Event from foreground service: " + event.values[0]);
                    //stopSelf();
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(sensorListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        System.out.println("REACHED ONSTARTCOMMAND");
        /* wake screen here */
        /*PowerManager pm = (PowerManager) getApplicationContext().getSystemService(getApplicationContext().POWER_SERVICE);
        String TAG = ForegroundService.class.getSimpleName();
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        wakeLock.acquire(30*1000L);*/



        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }
}