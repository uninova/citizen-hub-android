package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Objects;

public class Device implements Comparable<Device> {

    private final String address;
    private final int connectionKind;
    private final String name;

    public Device(String address, int connectionKind) {
        this(address, address, connectionKind);
    }

    public Device(String address, String name, int connectionKind) {
        this.address = address;
        this.name = name;
        this.connectionKind = connectionKind;
    }

    @Override
    public int compareTo(Device o) {
        int res = this.name.compareTo(o.name);

        if (res != 0)
            return res;

        res = this.connectionKind - o.connectionKind;

        if (res != 0)
            return res;

        return this.address.compareTo(o.address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return address.equals(device.address) && connectionKind == device.connectionKind && Objects.equals(name, device.name);
    }

    public String getAddress() {
        return address;
    }

    public int getConnectionKind() {
        return connectionKind;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, connectionKind, name);
    }
}
