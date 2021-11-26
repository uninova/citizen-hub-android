package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverters;

import java.util.Date;

public class LumbarAggregate {
    private MeasurementKind measurementKind = MeasurementKind.LUMBAR_EXTENSION_TRAINING;
    @TypeConverters({EpochTypeConverter.class})
    private Date timestamp;
    private Integer repetitions;
    private Long trainingLength;
    private Double score;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public MeasurementKind getMeasurementKind() {
        return measurementKind;
    }

    public void setMeasurementKind(MeasurementKind measurementKind) {
        this.measurementKind = measurementKind;
    }
}
