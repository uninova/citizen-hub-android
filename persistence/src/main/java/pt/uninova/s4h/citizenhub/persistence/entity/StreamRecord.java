package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "stream",
        primaryKeys = {"device_id", "measurement_type"},
        foreignKeys = @ForeignKey(
                onDelete = ForeignKey.CASCADE,
                entity = DeviceRecord.class,
                parentColumns = "id",
                childColumns = "device_id")
)
public class StreamRecord {

    @ColumnInfo(name = "device_id")
    @NonNull
    private Long deviceId;
    @ColumnInfo(name = "measurement_type")
    @NonNull
    private Integer measurementType;

    public StreamRecord(Long deviceId, Integer measurementType) {
        this.deviceId = deviceId;
        this.measurementType = measurementType;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Integer getMeasurementType() {
        return measurementType;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }
}