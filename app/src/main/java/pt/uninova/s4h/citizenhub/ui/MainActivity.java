package pt.uninova.s4h.citizenhub.ui;

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
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScannerListener;

public class MainActivity extends AppCompatActivity {

    private BluetoothDeviceManager bluetoothDeviceManager;

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
                bluetoothDeviceManager = binder.getDeviceManager();

                bluetoothDeviceManager.startDiscovery(new BluetoothScannerListener() {
                    @Override
                    public void onDeviceFound(String address, String name) {
                        Log.d("BLES", address + ":" + name);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bluetoothDeviceManager = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }

    private void startScan() {
        if (bluetoothDeviceManager != null) {
            bluetoothDeviceManager.startDiscovery(new BluetoothScannerListener() {
                @Override
                public void onDeviceFound(String address, String name) {
                    Log.d("BLES", address + ":" + name);
                }
            });
        }
    }

    private void connect(String address) {
        if (bluetoothDeviceManager != null) {
            bluetoothDeviceManager.connect(address);
        }
    }
}
