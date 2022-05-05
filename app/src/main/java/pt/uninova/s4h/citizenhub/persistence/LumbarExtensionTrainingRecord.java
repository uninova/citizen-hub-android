package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Instant;

@Entity(tableName = "lumbar_extension_training_measurement")
public class LumbarExtensionTrainingRecord {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @TypeConverters({EpochTypeConverter.class})
    private Instant timestamp;
    private Integer duration;
    private Float score;
    private Integer repetitions;
    private Integer weight;
    private Float calories;

    @Ignore
    public LumbarExtensionTrainingRecord(Integer timestamp, Integer duration, Float score, Integer repetitions, Integer weight, Float calories) {
        this(EpochTypeConverter.toInstant(timestamp.longValue()), duration, score, repetitions, weight, calories);
    }

    @Ignore
    public LumbarExtensionTrainingRecord(Long timestamp, Integer duration, Float score, Integer repetitions, Integer weight, Float calories) {
        this(EpochTypeConverter.toInstant(timestamp), duration, score, repetitions, weight, calories);
    }

    @Ignore
    public LumbarExtensionTrainingRecord(Instant timestamp, Integer duration, Float score, Integer repetitions, Integer weight, Float calories) {
        this(null, timestamp, duration, score, repetitions, weight, calories);
    }

    public LumbarExtensionTrainingRecord(Integer id, Instant timestamp, Integer duration, Float score, Integer repetitions, Integer weight, Float calories) {
        this.id = id;
        this.timestamp = timestamp;
        this.duration = duration;
        this.score = score;
        this.repetitions = repetitions;
        this.weight = weight;
        this.calories = calories;
    }

    public Float getCalories() {
        return calories;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public Float getScore() {
        return score;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setCalories(Float value) {
        this.calories = value;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}