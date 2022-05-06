package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "device")
public class DeviceRecord {

    @PrimaryKey
    @NonNull
    private String address;
    private String name;
    @ColumnInfo(name = "connection_kind")
    @TypeConverters(ConnectionKindTypeConverter.class)
    private ConnectionKind connectionKind;
    private String agent;

    public DeviceRecord(String address, String name, ConnectionKind connectionKind, String agent) {
        this.address = address;
        this.name = name;
        this.connectionKind = connectionKind;
        this.agent = agent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        DeviceRecord deviceRecord = (DeviceRecord) o;

        return address.equals(deviceRecord.address);
    }

    public String getAddress() {
        return address;
    }

    public String getAgent() {
        return agent;
    }

    public ConnectionKind getConnectionKind() {
        return connectionKind;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public void setAgent(String value) {
        this.agent = value;
    }

    public void setConnectionKind(ConnectionKind value) {
        this.connectionKind = value;
    }

    public void setName(String name) {
        this.name = name;
    }
}