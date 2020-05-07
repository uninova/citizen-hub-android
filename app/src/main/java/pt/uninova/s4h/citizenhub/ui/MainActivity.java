package pt.uninova.s4h.citizenhub.ui;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothDeviceManager;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothDeviceManagerService;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothDeviceManagerServiceBinder;

public class MainActivity extends AppCompatActivity {

    private BluetoothDeviceManagerService bluetoothDeviceManagerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startService();
    }

    private void startService() {
        Intent intent = new Intent(this, BluetoothDeviceManagerService.class);
        startService(intent);

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                BluetoothDeviceManagerServiceBinder binder = (BluetoothDeviceManagerServiceBinder) service;
                BluetoothDeviceManager bluetoothDeviceManager = binder.getDeviceManager();

                bluetoothDeviceManager.startScan(new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        Log.d("BLES", result.getDevice().getAddress());
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bluetoothDeviceManagerService = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
