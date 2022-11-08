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
import androidx.wear.ongoing.OngoingActivity;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "CitizenHubChannel";
    private int numberOfActiveSensors = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_content_title))
                .setContentText(input)
                .setSmallIcon(R.drawable.img_logo_figure)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_WORKOUT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .clearActions();
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        OngoingActivity ongoingActivity =
                new OngoingActivity.Builder(getApplicationContext(), 1, builder)
                        .setAnimatedIcon(R.drawable.img_logo_figure)
                        .setTouchIntent(pendingIntent)
                        .setTitle(getString(R.string.app_name))
                        .setOngoingActivityId(1)
                        .build();

        ongoingActivity.apply(getApplicationContext());
        manager.notify(1, notification);

        startForeground(1, notification);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorSteps = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor sensorHeartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        MainActivity.protocolSteps.observeForever(aBoolean -> updateNotificationMessage(pendingIntent));

        MainActivity.protocolHeartRate.observeForever(aBoolean -> updateNotificationMessage(pendingIntent));

        SensorEventListener stepsListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //TODO
                if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
                    System.out.println("Steps Sensor Event from foreground service: " + event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        SensorEventListener heartRateListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_HEART_RATE)
                    System.out.println("HR Sensor Event from foreground service: " + event.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(stepsListener, sensorSteps,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(heartRateListener, sensorHeartRate,
                SensorManager.SENSOR_DELAY_NORMAL);

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

    private void updateNotificationMessage(PendingIntent pendingIntent) {
        int numberOfSensorsMeasuring = 0;
        if (Boolean.TRUE.equals(MainActivity.protocolSteps.getValue()))
            numberOfSensorsMeasuring++;
        if (Boolean.TRUE.equals(MainActivity.protocolHeartRate.getValue()))
            numberOfSensorsMeasuring++;
        if (numberOfActiveSensors != numberOfSensorsMeasuring) {
            {
                numberOfActiveSensors = numberOfSensorsMeasuring;
                NotificationCompat.Builder notificationBuilderObserverHR = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle(getString(R.string.notification_content_title))
                        .setContentText(numberOfSensorsMeasuring + getString(R.string.notification_sensors_measuring))
                        .setSmallIcon(R.drawable.img_logo_figure)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_WORKOUT)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                Notification notificationObserverHR = notificationBuilderObserverHR.build();
                NotificationManager notificationManagerHR = getSystemService(NotificationManager.class);
                notificationManagerHR.notify(1, notificationObserverHR);
            }
        }
    }
}