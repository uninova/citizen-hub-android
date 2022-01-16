package pt.uninova.s4h.citizenhub.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentFactory;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgentFactory;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.DeviceRecord;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CitizenHubService extends LifecycleService implements WearOSServiceBound {

    boolean mBound = false;
    private final static CharSequence NOTIFICATION_TITLE = "Citizen Hub";
    private AgentOrchestrator orchestrator;
    private NotificationManager notificationManager;
    private WearOSMessageService wearOSMessageService;
    private WearOSServiceBinder wearOSServiceBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WearOSServiceBinder binder = (WearOSServiceBinder) service;
            wearOSMessageService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


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

    public AgentOrchestrator getAgentOrchestrator() {
        return orchestrator;
    }

    public WearOSMessageService getWearOSMessageService() {

        return wearOSMessageService;
    }

    private void initAgentOrchestrator() {
        final Map<ConnectionKind, AgentFactory<? extends Agent>> agentFactoryMap = new HashMap<>();

        agentFactoryMap.put(ConnectionKind.BLUETOOTH, new BluetoothAgentFactory(this));

        orchestrator = new AgentOrchestrator(agentFactoryMap);

        DeviceRepository deviceRepository = new DeviceRepository(getApplication());
        FeatureRepository featureRepository = new FeatureRepository(getApplication());
        MeasurementRepository measurementRepository = new MeasurementRepository(getApplication());

        deviceRepository.obtainAll(value -> {
            for (DeviceRecord i : value) {
                orchestrator.add(new Device(i.getAddress(), i.getName(), i.getConnectionKind()), agent -> {
                    agent.enable();
                    featureRepository.obtainKindsFromDevice(i.getAddress(), kindList -> {
                        for (MeasurementKind kind : kindList) {
                            agent.enableMeasurement(kind, measurementRepository::add);
                        }
                    });
                });
            }
        });
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
        wearOSMessageService = new WearOSMessageService();

        initAgentOrchestrator();
    }


    @Override
    public void onDestroy() {
        orchestrator.clear();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public WearOSMessageService getService() {
        return wearOSMessageService;
    }
}
