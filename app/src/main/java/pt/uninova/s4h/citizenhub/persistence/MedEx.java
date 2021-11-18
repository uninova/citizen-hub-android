package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "medEx")
public class MedEx {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @TypeConverters({EpochTypeConverter.class})
    private Date timestamp;
    private Integer repetitions;
    private Long trainingLength;
    private Double score;

    @Ignore
    public MedEx(Date timestamp,Integer repetitions, Long trainingLength, Double score) {
        this(null, timestamp,repetitions,trainingLength, score);
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

    public MedEx(Integer id, Date timestamp, Integer repetitions, Long trainingLength, Double score) {
        this.id = id;
        this.timestamp = timestamp;
        this.repetitions = repetitions;
        this.trainingLength = trainingLength;
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