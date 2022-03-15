package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity(tableName = "blood_pressure", primaryKeys = {"device_address", "kind_id"})
public class BloodPressureRecord {
    @NonNull
    private String device_address;
    private LocalDateTime timestamp;
    @ColumnInfo(name = "kind_id")
    @TypeConverters(MeasurementKindTypeConverter.class)
    @NonNull
    private MeasurementKind kind;
    private double value;
    private Integer isWorking;


    @Ignore
    public BloodPressureRecord() {
        device_address = null;
        kind = null;
        this.timestamp =null;
        this.isWorking =null;
    }

    public BloodPressureRecord(@NotNull String device_address, @NotNull MeasurementKind kind, LocalDateTime timestamp, double value, Integer isWorking) {
        this.device_address = device_address;
        this.kind = kind;
        this.timestamp = timestamp;
        this.value = value;
        this.isWorking = isWorking;
    }

    @NotNull
    public MeasurementKind getKind() {
        return kind;
    }

    public void setKind(@NotNull MeasurementKind kind) {
        this.kind = kind;
    }

    @NotNull
    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = this.device_address;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Integer getIsWorking() {
        return isWorking;
    }

    public void setIsWorking(Integer isWorking) {
        this.isWorking = isWorking;
    }
}
