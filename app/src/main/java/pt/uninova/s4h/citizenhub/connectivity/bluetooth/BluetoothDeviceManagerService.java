package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import pt.uninova.s4h.citizenhub.R;


public class BluetoothDeviceManagerService extends Service {

    private final static CharSequence NOTIFICATION_TITLE = "Citizen Hub";

    private BluetoothDeviceManager bluetoothDeviceManager;
    private NotificationManager notificationManager;

    public BluetoothDeviceManagerService() {
        super();
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, BluetoothDeviceManager.class.getCanonicalName())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NOTIFICATION_TITLE)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(BluetoothDeviceManagerService.class.getCanonicalName(), NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public BluetoothDeviceManager getBluetoothDeviceManager() {
        return bluetoothDeviceManager;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BluetoothDeviceManagerServiceBinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        bluetoothDeviceManager = new BluetoothDeviceManager(this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();
        startForeground(1, buildNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
