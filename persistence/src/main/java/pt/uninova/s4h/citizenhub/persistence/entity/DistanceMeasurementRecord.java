package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import pt.uninova.s4h.citizenhub.data.DistanceMeasurement;

@Entity(tableName = "distance_measurement",
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class DistanceMeasurementRecord {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @ColumnInfo(name = "sample_id")
    @NonNull
    private Long sampleId;

    @NonNull
    private Double value;

    public DistanceMeasurementRecord(Long id, Long sampleId, Double value) {
        this.id = id;
        this.sampleId = sampleId;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public Double getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public DistanceMeasurement toDistanceMeasurement() {
        return new DistanceMeasurement(value);
    }
}
