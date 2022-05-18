package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "feature", primaryKeys = {"device_address", "kind_id"}/*, foreignKeys = @ForeignKey(onDelete = CASCADE, entity = Device.class, parentColumns = "address", childColumns = "device_address")*/)
public class Feature {
    @NonNull
    private String device_address;
    @ColumnInfo(name = "kind_id")
    @NonNull
    private Integer kind;

    @Ignore
    public Feature() {
        device_address = null;
        kind = null;
    }

    public Feature(String device_address, Integer kind) {
        this.device_address = device_address;
        this.kind = kind;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = this.device_address;
    }
}