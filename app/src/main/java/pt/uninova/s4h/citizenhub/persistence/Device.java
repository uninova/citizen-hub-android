package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import java.util.Objects;

@Entity(tableName = "device", primaryKeys = {"device_address", "connection_kind"})
public class Device {

    private String name;
    @NonNull
    private String address;
    @ColumnInfo(name = "connection_kind")
    @TypeConverters(ConnectionKindTypeConverter.class)
    private int connectionKind;
    private String state;

    @Ignore
    public Device() {
        address = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return address.equals(device.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    public Device(String name, String address, int connectionKind, String state) {
        this.name = name;
        this.address = address;
        this.connectionKind = connectionKind;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConnectionKind() {
        return connectionKind;
    }

    public void setConnectionKind(int connectionKind) {
        this.connectionKind = connectionKind;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}