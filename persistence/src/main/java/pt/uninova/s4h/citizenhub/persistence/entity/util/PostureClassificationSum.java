package pt.uninova.s4h.citizenhub.persistence.entity.util;

import androidx.room.TypeConverters;

import java.time.Duration;

import pt.uninova.s4h.citizenhub.persistence.conversion.DurationTypeConverter;

public class PostureClassificationSum {

    private Integer classification;
    @TypeConverters(DurationTypeConverter.class)
    private Duration duration;

    public PostureClassificationSum(Integer classification, Duration duration) {
        this.classification = classification;
        this.duration = duration;
    }

    public Integer getClassification() {
        return classification;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
