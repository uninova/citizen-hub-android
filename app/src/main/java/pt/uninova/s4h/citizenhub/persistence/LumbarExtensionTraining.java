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
    private Date timestamp;
    private Integer trainingLength;
    private Float score;
    private Integer repetitions;

    @Ignore
    public LumbarExtensionTraining(Long timestamp, Integer trainingLength, Float score, Integer repetitions) {
        this(EpochTypeConverter.toDate(timestamp), trainingLength, score, repetitions);
    }

    @Ignore
    public LumbarExtensionTraining(Date timestamp, Integer trainingLength, Float score, Integer repetitions) {
        this(null, timestamp, trainingLength, score, repetitions);
    }

    public LumbarExtensionTraining(Integer id, Date timestamp, Integer trainingLength, Float score, Integer repetitions) {
        this.id = id;
        this.timestamp = timestamp;
        this.trainingLength = trainingLength;
        this.score = score;
        this.repetitions = repetitions;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}