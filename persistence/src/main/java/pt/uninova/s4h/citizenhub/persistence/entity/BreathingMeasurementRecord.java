package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "breathing_measurement",
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class BreathingMeasurementRecord {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @ColumnInfo(name = "sample_id")
    @NonNull
    private Long sampleId;

    @NonNull
    private Integer index;
    @NonNull
    private Integer type;
    @NonNull
    private Double value;

    public BreathingMeasurementRecord(Long id, Long sampleId, Integer index, Integer type, Double value) {
        this.id = id;
        this.sampleId = sampleId;
        this.index = index;
        this.type = type;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Integer getIndex() {
        return index;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public Integer getType() {
        return type;
    }

    public Double getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
