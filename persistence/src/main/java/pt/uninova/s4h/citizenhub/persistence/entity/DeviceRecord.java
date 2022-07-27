package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "device", indices = {@Index(value = "address", unique = true)})
public class DeviceRecord {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @NonNull
    private String address;
    private String name;
    @ColumnInfo(name = "connection_kind")
    @NonNull
    private Integer connectionKind;
    private String agent;

    public DeviceRecord(Long id, String address, String name, Integer connectionKind, String agent) {
        this.id = id;
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

    public Long getId() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}