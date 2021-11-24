package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "lumbar_training")
public class LumbarExtensionTraining {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @TypeConverters({EpochTypeConverter.class})
    private Date timestamp;
    private Integer repetitions;
    private Long trainingLength;
    private Double score;

    @Ignore
    public LumbarExtensionTraining(Date timestamp, Integer repetitions, Long trainingLength, Double score) {
        this(null, timestamp,repetitions,trainingLength, score);
    }

    public LumbarExtensionTraining(Integer id, Date timestamp, Integer repetitions, Long trainingLength, Double score) {
        this.id = id;
        this.timestamp = timestamp;
        this.repetitions = repetitions;
        this.trainingLength=trainingLength;
        this.score=score;
    }


    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Long getTrainingLength() {
        return trainingLength;
    }

    public void setTrainingLength(Long trainingLength) {
        this.trainingLength = trainingLength;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    //remove date

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