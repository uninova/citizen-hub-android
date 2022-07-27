package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "heart_rate_measurement",
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class HeartRateMeasurementRecord {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @ColumnInfo(name = "sample_id")
    @NonNull
    private Long sampleId;

    @NonNull
    private Integer value;

    public HeartRateMeasurementRecord(Long id, Long sampleId, Integer value) {
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

    public Integer getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
