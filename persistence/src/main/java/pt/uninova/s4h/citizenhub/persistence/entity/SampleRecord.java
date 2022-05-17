package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Instant;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;

@Entity(tableName = "sample",
        foreignKeys = {@ForeignKey(entity = DeviceRecord.class, parentColumns = "id", childColumns = "device_id", onDelete = ForeignKey.SET_NULL)},
        indices = {@Index("device_id")})
public class SampleRecord {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @TypeConverters(EpochTypeConverter.class)
    private Instant timestamp;
    @ColumnInfo(name = "device_id")
    private Integer deviceId;

    @Ignore
    public SampleRecord(Instant timestamp, Integer deviceId) {
        this(null, timestamp, deviceId);
    }

    public SampleRecord(Long id, Instant timestamp, Integer deviceId) {
        this.id = id;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public Long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setDeviceId(Integer value) {
        deviceId = value;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public void setTimestamp(Instant value) {
        this.timestamp = value;
    }
}
