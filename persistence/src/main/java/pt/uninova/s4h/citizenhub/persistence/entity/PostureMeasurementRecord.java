package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Duration;

import pt.uninova.s4h.citizenhub.persistence.conversion.DurationTypeConverter;

@Entity(tableName = "posture_measurement",
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class PostureMeasurementRecord {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;
    @ColumnInfo(name = "sample_id")
    @NonNull
    private Long sampleId;

    @NonNull
    private Integer classification;
    @TypeConverters(DurationTypeConverter.class)
    @NonNull
    private Duration duration;

    public PostureMeasurementRecord(Long id, Long sampleId, Integer classification, Duration duration) {
        this.id = id;
        this.sampleId = sampleId;
        this.classification = classification;
        this.duration = duration;
    }

    public Integer getClassification() {
        return classification;
    }

    public Duration getDuration() {
        return duration;
    }

    public Long getId() {
        return id;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }
}
