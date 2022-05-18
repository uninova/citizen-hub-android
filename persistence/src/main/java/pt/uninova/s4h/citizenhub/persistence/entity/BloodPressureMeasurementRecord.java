package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "blood_pressure_measurement",
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class BloodPressureMeasurementRecord {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "sample_id")
    private Long sampleId;

    private Double systolic;
    private Double diastolic;
    @ColumnInfo(name = "mean_arterial_pressure")
    private Double meanArterialPressure;

    public BloodPressureMeasurementRecord(Long id, Long sampleId, Double systolic, Double diastolic, Double meanArterialPressure) {
        this.id = id;
        this.sampleId = sampleId;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.meanArterialPressure = meanArterialPressure;
    }

    public Double getDiastolic() {
        return diastolic;
    }

    public Long getId() {
        return id;
    }

    public Double getMeanArterialPressure() {
        return meanArterialPressure;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public Double getSystolic() {
        return systolic;
    }

    public void setDiastolic(Double diastolic) {
        this.diastolic = diastolic;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMeanArterialPressure(Double meanArterialPressure) {
        this.meanArterialPressure = meanArterialPressure;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public void setSystolic(Double systolic) {
        this.systolic = systolic;
    }
}
