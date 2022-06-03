package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "device_enabled_measurement", primaryKeys = {"device_address", "measurement_type"}, foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE, entity = DeviceRecord.class, parentColumns = "id", childColumns = "device_id"))
public class DeviceEnabledMeasurement {

    @ColumnInfo(name = "device_id")
    private Long deviceId;
    @ColumnInfo(name = "measurement_type")
    private Integer measurementType;

    public DeviceEnabledMeasurement(Long deviceId, Integer measurementType) {
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