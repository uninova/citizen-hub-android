package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

public interface BluetoothScannerListener {

    void onDeviceFound(BluetoothDeviceDescriptor device);

}
