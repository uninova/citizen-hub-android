package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Objects;

@Entity(tableName = "device", indices = {@Index(value = "address", unique = true)})
public class DeviceRecord {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String address;
    private String name;
    @ColumnInfo(name = "connection_kind")
    private Integer connectionKind;
    private String agent;

    public DeviceRecord(String address, String name, Integer connectionKind, String agent) {
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

    public Integer getConnectionKind() {
        return connectionKind;
    }

    public Integer getId() {
        return id;
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

    public void setConnectionKind(Integer value) {
        this.connectionKind = value;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public void setName(String name) {
        this.name = name;
    }
}