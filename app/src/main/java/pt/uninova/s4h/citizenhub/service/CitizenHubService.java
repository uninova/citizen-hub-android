package pt.uninova.s4h.citizenhub.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import pt.uninova.s4h.citizenhub.connectivity.DeviceManager;
import pt.uninova.s4h.citizenhub.report.ReportManager;
import pt.uninova.s4h.citizenhub.storage.DatabaseManager;
import pt.uninova.s4h.citizenhub.ui.R;

public class CitizenHubService extends Service {

    private final static CharSequence NOTIFICATION_TITLE = "Citizen Hub";

    private NotificationManager notificationManager;

    private DeviceManager deviceManager;
    private ReportManager reportManager;
    private DatabaseManager databaseManager;

    public CitizenHubService() {
        super();
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CitizenHubService.class.getCanonicalName())
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

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CitizenHubServiceBinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();
        startForeground(1, buildNotification());

        deviceManager = new DeviceManager();
        reportManager = new ReportManager(Environment.getExternalStorageDirectory().toString());
        databaseManager = new DatabaseManager(getApplicationContext(), "CitizenHubDatabase.db");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        deviceManager.close();
        reportManager.close();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
