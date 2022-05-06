package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BluetoothScanner extends ScanCallback {

    private final Map<String, BluetoothDevice> devices;
    private final BluetoothLeScanner scanner;
    private BluetoothScannerListener listener;

    public BluetoothScanner(BluetoothManager manager) {
        devices = new HashMap<>();
        scanner = manager.getAdapter().getBluetoothLeScanner();
        listener = null;
    }

    private void addDevice(BluetoothDevice device) throws SecurityException {
        final String address = device.getAddress();
        final String name = device.getName();

        if (!devices.containsKey(address)) {
            devices.put(address, device);
            listener.onDeviceFound(address, name);
        }
    }

    public void clearDevices() {
        devices.clear();
    }

    public BluetoothDevice getDevice(String address) {
        return devices.get(address);
    }

    public boolean foundDevice(String address) {
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
    public synchronized void onScanFailed(int errorCode) throws SecurityException {
        scanner.stopScan(this);

        listener = null;
    }

    @Override
    public synchronized void onScanResult(int callbackType, ScanResult result) throws SecurityException {
        if (result.getDevice().getName() != null) // Ignore null names
            addDevice(result.getDevice());
    }

    public synchronized void start(BluetoothScannerListener listener) throws SecurityException {
        if (!isScanning()) {
            this.listener = listener;
            scanner.startScan(this);
        }
    }

    public synchronized void start(BluetoothScannerListener listener, UUID serviceUUID) throws SecurityException {
        if (!isScanning()) {
            this.listener = listener;

            final ScanFilter uuidScanFilter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(serviceUUID))
                    .build();

            final ScanSettings scanSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();

            scanner.startScan(Collections.singletonList(uuidScanFilter), scanSettings, this);
        }
    }

    public synchronized void stop() throws SecurityException {
        if (isScanning()) {
            scanner.stopScan(this);
            listener = null;
        }
    }
}
