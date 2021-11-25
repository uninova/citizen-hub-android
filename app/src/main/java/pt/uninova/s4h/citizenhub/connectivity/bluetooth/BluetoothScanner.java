package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;

import java.util.ArrayList;
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

    private void addDevice(BluetoothDevice device) {
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

    public synchronized void startWithFilter(BluetoothScannerListener listener,ParcelUuid serviceUUID) {
        if (!isScanning()) {
            this.listener = listener;
            scanner.startScan(buildScanFilters(serviceUUID),buildScanSettings(),this );
        }
    }

    private List<ScanFilter> buildScanFilters(ParcelUuid serviceUUID) {
        List<ScanFilter> scanFilters = new ArrayList<>();
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setServiceUuid(serviceUUID);
        scanFilters.add(builder.build());
        return scanFilters;
    }

    private ScanSettings buildScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        return builder.build();
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
