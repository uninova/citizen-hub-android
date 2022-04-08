package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import pt.uninova.s4h.citizenhub.R;

@Entity(tableName ="blood_pressure",
        indices = @Index(value = {"id"}, unique = true))

public class BloodPressureRecord {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @TypeConverters({SampleTypeConverter.class})
    @ColumnInfo(name="sample_id")
    private final SampleRecord sampleRecord;
    private final double systolic;
    private final double diastolic;
    private final double meanArterialPressure;

//    @Ignore
//    public BloodPressureRecord(SampleRecord sampleRecord) {
//        this.sampleRecord = sampleRecord;
//        this.id=null;
//        this.systolic = 0;
//        this. diastolic = 0;
//        this.meanArterialPressure = 0;
//    }

    public BloodPressureRecord(SampleRecord sampleRecord, double systolic, double diastolic, double meanArterialPressure) {
        this.id = null;
        this.sampleRecord = sampleRecord;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.meanArterialPressure = meanArterialPressure;
    }

    public BloodPressureRecord(Integer id, SampleRecord sampleRecord, double systolic, double diastolic, double meanArterialPressure) {
        this.id = id;
        this.sampleRecord = sampleRecord;
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

    public SampleRecord getSample() {
        return sampleRecord;
    }
}
