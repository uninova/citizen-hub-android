package pt.uninova.s4h.citizenhub.persistence.entity.util;

import androidx.room.TypeConverters;

import java.time.Duration;

import pt.uninova.s4h.citizenhub.persistence.conversion.DurationTypeConverter;

public class LumbarExtensionTrainingSummary {

    @TypeConverters(DurationTypeConverter.class)
    private Duration duration;
    private Integer repetitions;
    private Integer weight;
    private Double calories;

    public LumbarExtensionTrainingSummary(Duration duration, Integer repetitions, Integer weight, Double calories) {
        this.duration = duration;
        this.repetitions = repetitions;
        this.weight = weight;
        this.calories = calories;
    }

    public Double getCalories() {
        return calories;
    }

    public Duration getDuration() {
        return duration;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
