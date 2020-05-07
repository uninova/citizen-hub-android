package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothScanner extends ScanCallback {

    private final Map<String, BluetoothDevice> devices;
    private final BluetoothLeScanner scanner;

    private BluetoothScannerListener listener;

    public BluetoothScanner(BluetoothManager manager) {
        devices = new HashMap<>();
        scanner = manager.getAdapter().getBluetoothLeScanner();

        listener = null;
    }

    private void addDevice(BluetoothDevice device) {
        final String address = device.getAddress();
        final String name = device.getName();

        if (!devices.containsKey(address)) {
            devices.put(device.getAddress(), device);
            listener.onDeviceFound(new BluetoothDeviceDescriptor(address, name));
        }
    }

    public BluetoothDevice getDevice(String address) {
        return devices.get(address);
    }

    public boolean hasDevice(String address) {
        return devices.containsKey(address);
    }

    public boolean isScanning() {
        return listener != null;
    }

    @Override
    public synchronized void onBatchScanResults(List<ScanResult> results) {
        for (ScanResult i : results) {
            addDevice(i.getDevice());
        }
    }

    @Override
    public synchronized void onScanFailed(int errorCode) {
        scanner.stopScan(this);
        listener = null;
    }

    @Override
    public synchronized void onScanResult(int callbackType, ScanResult result) {
        addDevice(result.getDevice());
    }

    public synchronized void start(BluetoothScannerListener listener) {
        if (!isScanning()) {
            this.listener = listener;
            scanner.startScan(this);
        }
    }

    public synchronized void stop() {
        if (isScanning()) {
            scanner.stopScan(this);
            listener = null;
        }
    }
}
