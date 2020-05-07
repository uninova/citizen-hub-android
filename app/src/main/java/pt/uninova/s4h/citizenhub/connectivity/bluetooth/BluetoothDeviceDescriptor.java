package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

public class BluetoothDeviceDescriptor {

    private final String address;
    private final String name;

    public BluetoothDeviceDescriptor(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
