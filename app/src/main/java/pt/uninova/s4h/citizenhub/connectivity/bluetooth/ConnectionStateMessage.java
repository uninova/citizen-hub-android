package pt.uninova.s4h.citizenhub.connectivity.bluetooth;


import pt.uninova.s4h.citizenhub.persistence.Device;

public class ConnectionStateMessage {
    public Device device;
    public BluetoothConnectionState state;

    public ConnectionStateMessage(Device device, BluetoothConnectionState state) {
        this.device = device;
        this.state = state;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public BluetoothConnectionState getState() {
        return state;
    }

    public void setState(BluetoothConnectionState state) {
        this.state = state;
    }
}

