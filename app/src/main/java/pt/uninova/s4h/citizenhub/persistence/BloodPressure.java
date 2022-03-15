package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity(tableName = "blood_pressure", primaryKeys = {"device_address", "kind_id"})
public class BloodPressure {
    @NonNull
    private String device_address;
    private LocalDateTime timestamp;
    @ColumnInfo(name = "kind_id")
    @TypeConverters(MeasurementKindTypeConverter.class)
    @NonNull
    private MeasurementKind kind;
    private double systolic;
    private double dystolic;
    private double pulse_rate;


    @Ignore
    public BloodPressure() {
        device_address = null;
        kind = null;
    }

    public BloodPressure(@NotNull String device_address, @NotNull MeasurementKind kind, LocalDateTime timestamp, double systolic, double dystolic, double pulse_rate) {
        this.device_address = device_address;
        this.kind = kind;
        this.timestamp = timestamp;
        this.systolic = systolic;
        this.dystolic = dystolic;
        this.pulse_rate = pulse_rate;
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

    public double getSystolic() {
        return systolic;
    }

    public void setSystolic(double systolic) {
        this.systolic = systolic;
    }

    public double getDystolic() {
        return dystolic;
    }

    public void setDystolic(double dystolic) {
        this.dystolic = dystolic;
    }

    public double getPulse_rate() {
        return pulse_rate;
    }

    public void setPulse_rate(double pulse_rate) {
        this.pulse_rate = pulse_rate;
    }
}
