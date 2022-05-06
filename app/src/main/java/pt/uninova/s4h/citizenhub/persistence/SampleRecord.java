package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Instant;

@Entity(tableName = "sample",
        foreignKeys = {@ForeignKey(entity = DeviceRecord.class, parentColumns = "address", childColumns = "device_address", onDelete = ForeignKey.SET_NULL)},
        indices = {@Index("device_address")})
public class SampleRecord {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @TypeConverters(EpochTypeConverter.class)
    private Instant timestamp;
    @ColumnInfo(name = "device_address")
    private String deviceAddress;

    @Ignore
    public SampleRecord(Instant timestamp, String deviceAddress) {
        this(null, timestamp, deviceAddress);
    }

    public SampleRecord(Integer id, Instant timestamp, String deviceAddress) {
        this.id = id;
        this.timestamp = timestamp;
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public Integer getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setDeviceAddress(String value) {
        deviceAddress = value;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public void setTimestamp(Instant value) {
        this.timestamp = value;
    }
}
