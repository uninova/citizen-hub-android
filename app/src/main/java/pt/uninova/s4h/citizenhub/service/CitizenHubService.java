package pt.uninova.s4h.citizenhub.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import java.util.Objects;

import pt.uninova.s4h.citizenhub.MainActivity;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;

public class CitizenHubService extends LifecycleService {

    private final static CharSequence NOTIFICATION_TITLE = "Citizen Hub";
    private AgentOrchestrator agentOrchestrator;
    private NotificationManager notificationManager;
    private final String ACTION_STOP_SERVICE = "STOP";

    public CitizenHubService() {
        super();
    }

    private Notification buildNotification() {

        Intent stopSelf = new Intent(this, MainActivity.class);
        stopSelf.setAction(ACTION_STOP_SERVICE);

        PendingIntent pStopSelf = PendingIntent
                .getService(this, 0, stopSelf
                        , PendingIntent.FLAG_CANCEL_CURRENT);


        return new NotificationCompat.Builder(this, Objects.requireNonNull(CitizenHubService.class.getCanonicalName()))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NOTIFICATION_TITLE)
                .addAction(R.mipmap.ic_launcher, "Close", pStopSelf)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CitizenHubService.class.getCanonicalName(), NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public AgentOrchestrator getAgentOrchestrator() {
        return agentOrchestrator;
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);

        return new CitizenHubServiceBinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        startForeground(1, buildNotification());

        agentOrchestrator = new AgentOrchestrator(this);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        startForeground(1, buildNotification());

        agentOrchestrator = new AgentOrchestrator(this);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        if (ACTION_STOP_SERVICE.equals(intent.getAction())) {
            stopForegroundService();
        }
        return START_STICKY;
    }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }
}
