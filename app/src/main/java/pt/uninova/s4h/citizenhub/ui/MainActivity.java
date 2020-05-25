package pt.uninova.s4h.citizenhub.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBinder;

public class MainActivity extends AppCompatActivity {

    private CitizenHubServiceBinder binder;
    private ServiceConnection serviceConnection;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        testButton = findViewById(R.id.TestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceConnection == null) {
                    startService();
                } else {
                    stopService();
                }
            }
        });

        startService();
    }

    private void startService() {
        Intent intent = new Intent(this, CitizenHubService.class);
        startService(intent);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (CitizenHubServiceBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;
            }
        };

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopService() {
        Intent intent = new Intent(this, CitizenHubService.class);

        unbindService(serviceConnection);
        stopService(intent);

        serviceConnection = null;
    }
}
