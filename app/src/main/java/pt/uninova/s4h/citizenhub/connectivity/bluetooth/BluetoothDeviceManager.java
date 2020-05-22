package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class BluetoothDeviceManager {

    private final Context context;
    private final BluetoothManager manager;

    private final BluetoothScanner bluetoothScanner;

    public BluetoothDeviceManager(Context context) {
        this.context = context;
        manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothScanner = new BluetoothScanner(manager);
    }

    public void connect(String address) {
        connect(address, new BluetoothConnection());
    }

    public void connect(String address, BluetoothConnection connection) {
        manager.getAdapter().getRemoteDevice(address).connectGatt(context, true, connection);
    }

    public void disconnect(String address) {

    }

    public void startDiscovery(BluetoothScannerListener listener) {
        bluetoothScanner.start(listener);
    }

    public void stopDiscovery() {
        bluetoothScanner.stop();
    }
    
}
