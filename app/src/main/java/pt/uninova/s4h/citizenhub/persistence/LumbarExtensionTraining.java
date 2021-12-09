package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import pt.uninova.util.time.LocalDateInterval;

@Entity(tableName = "lumbar_training")
public class LumbarExtensionTraining {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @TypeConverters({EpochTypeConverter.class})
    private LocalDateTime timestamp;
    private Integer trainingLength;
    private Float score;
    private Integer repetitions;
    private Integer weight;

    @Ignore
    public LumbarExtensionTraining(Integer timestamp, Integer trainingLength, Float score, Integer repetitions, Integer weight) {
        this(EpochTypeConverter.toLocalDateTime(timestamp), trainingLength, score, repetitions,weight);
    }

    @Ignore
    public LumbarExtensionTraining(Long timestamp, Integer trainingLength, Float score, Integer repetitions, Integer weight) {
        this(EpochTypeConverter.toLocalDateTime(timestamp), trainingLength, score, repetitions,weight);
    }

    @Ignore
    public LumbarExtensionTraining(LocalDateTime timestamp, Integer trainingLength, Float score, Integer repetitions, Integer weight) {
        this(null, timestamp, trainingLength, score, repetitions, weight);
    }

    public LumbarExtensionTraining(Integer id, LocalDateTime timestamp, Integer trainingLength, Float score, Integer repetitions, Integer weight) {
        this.id = id;
        this.timestamp = timestamp;
        this.trainingLength = trainingLength;
        this.score = score;
        this.repetitions = repetitions;
        this.weight = weight;
    }


    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getTrainingLength() {
        return trainingLength;
    }

    public void setTrainingLength(Integer trainingLength) {
        this.trainingLength = trainingLength;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}