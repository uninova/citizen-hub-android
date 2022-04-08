package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(tableName = "sampleRecord", indices = @Index(value = {"id"}, unique = true))
public class SampleRecord {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private final Instant timestamp;
    @ColumnInfo(name = "device_address")
    //TODO no futuro marcar como FK
    private final String deviceAddress;

    //se queixar tirar o final e acrescentar sets.

    public SampleRecord(Instant timestamp, String device_address) {
        this.id = null;
        this.timestamp = timestamp;
        this.deviceAddress = device_address;
    }
    public SampleRecord(Integer id, Instant timestamp, String device_address) {
        this.id = id;
        this.timestamp = timestamp;
        this.deviceAddress = device_address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }


    public String getDeviceAddress() {
        return deviceAddress;
    }
}
