package pt.uninova.s4h.citizenhub.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScannerListener;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class CitizenHubService extends Service {

    private final static CharSequence NOTIFICATION_TITLE = "Citizen Hub";
    private static BluetoothManager mBluetoothManager;
    private NotificationManager notificationManager;
    private BluetoothScannerListener listener;

    public CitizenHubService() {
        super();
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, Objects.requireNonNull(CitizenHubService.class.getCanonicalName()))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NOTIFICATION_TITLE)
                .build();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CitizenHubService.class.getCanonicalName(), NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CitizenHubServiceBinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        SamplingCode();
        startForeground(1, buildNotification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void StartScan() {
        BluetoothScanner bs = new BluetoothScanner(mBluetoothManager, getApplication());
        bs.start(listener);
    }


    public void SamplingCode() {
        Handler handler = new Handler(getMainLooper());
        int delay = 10000;
        final Random random = new Random();
        final MeasurementRepository repo = new MeasurementRepository(getApplication());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MeasurementKind kind = MeasurementKind.find(random.nextInt(7));

                Date date = new Date();

                Measurement measurement = new Measurement(date, kind, (double) random.nextInt(200));
                System.out.println(measurement.getKind().toString() + ":" + measurement.getTimestamp().toString() + ":" + measurement.getValue());

                repo.add(measurement);

                handler.postDelayed(this, random.nextInt(delay));
            }
        }, random.nextInt(delay));
        //
        // END SAMPLING CODE
        //
    }

}
