package pt.uninova.s4h.citizenhub.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBinder;

public class MainActivity extends AppCompatActivity {

    private CitizenHubServiceBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startService();
    }

    private void startService() {
        Intent intent = new Intent(this, CitizenHubService.class);
        startService(intent);

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (CitizenHubServiceBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
