package pt.uninova.s4h.citizenhub.persistence;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Entity(tableName = "blood_pressure",
        indices = @Index(value = {"id"}, unique = true),
        foreignKeys = @ForeignKey(
                entity = Sample.class,
                parentColumns = "id",
                childColumns = "id",
                onUpdate = CASCADE, onDelete = CASCADE))

public class BloodPressureMeasurement {
    @PrimaryKey
    private Integer id;
    private final double systolic;
    private final double diastolic;
    private final double meanArterialPressure;


    @Ignore
    public BloodPressureMeasurement() {
        this.id=null;
        this.systolic = 0;
        this. diastolic = 0;
        this.meanArterialPressure = 0;
    }

    public BloodPressureMeasurement(Integer id, double systolic, double diastolic, double meanArterialPressure) {
        this.id = id;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.meanArterialPressure = meanArterialPressure;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSystolic() {
        return systolic;
    }

    public double getDiastolic() {
        return diastolic;
    }

    public double getMeanArterialPressure() {
        return meanArterialPressure;
    }
}
