package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Duration;

import pt.uninova.s4h.citizenhub.data.LumbarExtensionTrainingMeasurement;
import pt.uninova.s4h.citizenhub.data.LumbarExtensionTrainingValue;
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
    @NonNull
    private Long id;

    @ColumnInfo(name = "sample_id")
    @NonNull
    private Long sampleId;

    @TypeConverters(DurationTypeConverter.class)
    @NonNull
    private Duration duration;
    @NonNull
    private Double score;
    @NonNull
    private Integer repetitions;
    @NonNull
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

    public LumbarExtensionTrainingMeasurement toLumbarExtensionTrainingMeasurement() {
        return new LumbarExtensionTrainingMeasurement(new LumbarExtensionTrainingValue(duration, score, repetitions, weight));
    }
}