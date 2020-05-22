package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.os.Binder;

public class BluetoothDeviceManagerServiceBinder extends Binder {

    private final BluetoothDeviceManagerService service;

    public BluetoothDeviceManagerServiceBinder(BluetoothDeviceManagerService service) {
        this.service = service;
    }

    public BluetoothDeviceManager getDeviceManager() {
        return service.getBluetoothDeviceManager();
    }

}
