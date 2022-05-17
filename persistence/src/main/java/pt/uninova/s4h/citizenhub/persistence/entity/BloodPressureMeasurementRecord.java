package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "blood_pressure_measurement")
public class BloodPressureMeasurementRecord {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "sample_id")
    private Integer sampleId;

    private Double systolic;
    private Double diastolic;
    @ColumnInfo(name = "mean_arterial_pressure")
    private Double meanArterialPressure;

    @Ignore
    public BloodPressureMeasurementRecord(Integer sampleId, Double systolic, Double diastolic, Double meanArterialPressure) {
        this(null, sampleId, systolic, diastolic, meanArterialPressure);
    }

    public BloodPressureMeasurementRecord(Integer id, Integer sampleId, Double systolic, Double diastolic, Double meanArterialPressure) {
        this.id = id;
        this.sampleId = sampleId;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.meanArterialPressure = meanArterialPressure;
    }

    public Double getDiastolic() {
        return diastolic;
    }

    public Integer getId() {
        return id;
    }

    public Double getMeanArterialPressure() {
        return meanArterialPressure;
    }

    public Integer getSampleId() {
        return sampleId;
    }

    public Double getSystolic() {
        return systolic;
    }

    public void setDiastolic(Double value) {
        this.diastolic = value;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public void setMeanArterialPressure(Double value) {
        this.meanArterialPressure = value;
    }

    public void setSampleId(Integer value) {
        this.sampleId = value;
    }

    public void setSystolic(Double value) {
        this.systolic = value;
    }
}
