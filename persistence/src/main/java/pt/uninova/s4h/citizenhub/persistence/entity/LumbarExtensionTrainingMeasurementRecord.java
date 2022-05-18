package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Duration;

import pt.uninova.s4h.citizenhub.persistence.conversion.DurationTypeConverter;

@Entity(tableName = "lumbar_extension_training_measurement",
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class LumbarExtensionTrainingMeasurementRecord {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "sample_id")
    private Long sampleId;

    @TypeConverters(DurationTypeConverter.class)
    private Duration duration;
    private Double score;
    private Integer repetitions;
    private Integer weight;

    public LumbarExtensionTrainingMeasurementRecord(Long id, Long sampleId, Duration duration, Double score, Integer repetitions, Integer weight) {
        this.id = id;
        this.sampleId = sampleId;
        this.duration = duration;
        this.score = score;
        this.repetitions = repetitions;
        this.weight = weight;
    }

    public Duration getDuration() {
        return duration;
    }

    public Long getId() {
        return id;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public Double getScore() {
        return score;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}