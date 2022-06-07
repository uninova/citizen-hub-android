package pt.uninova.s4h.citizenhub.persistence.entity.util;

import androidx.room.TypeConverters;

import java.time.Duration;
import java.time.Instant;

import pt.uninova.s4h.citizenhub.persistence.conversion.DurationTypeConverter;

public class ReportUtil {

    //Blood Pressure
    private Double systolic;
    private Double diastolic;
    private Double meanArterialPressure;
    private Double pulseRate;

    //Calories
    private Double calories;

    //Distance
    private Double distance;

    //Heart Rate
    private Double maxHeartRate;
    private Double minHeartRate;
    private Double avgHeartRate;

    //Lumbar Extension Training
    @TypeConverters(DurationTypeConverter.class)
    private Duration lumbarExtensionDuration;
    private Double lumbarExtensionScore;
    private Integer lumbarExtensionRepetitions;
    private Integer lumbarExtensionWeight;

    //Position
    private Integer postureClassification;
    @TypeConverters(DurationTypeConverter.class)
    private Duration postureDuration;
    @TypeConverters(DurationTypeConverter.class)
    private Duration correctPostureDuration;
    @TypeConverters(DurationTypeConverter.class)
    private Duration wrongPostureDuration;

    //Steps
    private Integer steps;

    //Timestamp
    private Instant timestamp;

    //Blood Pressure
    public Double getSystolic(){
        return systolic;
    }

    public void setSystolic(Double systolic) { this.systolic = systolic; }

    public Double getDiastolic(){
        return diastolic;
    }

    public void setDiastolic(Double diastolic) { this.diastolic = diastolic; }

    public Double getMeanArterialPressure(){
        return meanArterialPressure;
    }

    public void setMeanArterialPressure(Double meanArterialPressure) { this.meanArterialPressure = meanArterialPressure; }

    public Double getPulseRate(){
        return pulseRate;
    }

    public void setPulseRate(Double pulseRate) { this.pulseRate = pulseRate; }

    //Calories
    public Double getCalories(){ return calories; }

    public void setCalories(Double calories){ this.calories = calories;}

    //Distance
    public Double getDistance(){return distance;}

    public void setDistance(Double distance){this.distance = distance;}

    //Heart Rate
    public Double getMaxHeartRate(){
        return maxHeartRate;
    }

    public void setMaxHeartRate(Double maxHeartRate) { this.maxHeartRate = maxHeartRate; }

    public Double getMinHeartRate(){
        return minHeartRate;
    }

    public void setMinHeartRate(Double minHeartRate) { this.minHeartRate = minHeartRate; }

    public Double getAvgHeartRate(){
        return avgHeartRate;
    }

    public void setAvgHeartRate(Double avgHeartRate) { this.avgHeartRate = avgHeartRate; }

    //Lumbar Extension Training
    public Duration getLumbarExtensionDuration(){
        return lumbarExtensionDuration;
    }

    public void setLumbarExtensionDuration(Duration lumbarExtensionDuration) { this.lumbarExtensionDuration = lumbarExtensionDuration; }

    public Double getLumbarExtensionScore(){
        return lumbarExtensionScore;
    }

    public void setLumbarExtensionScore(Double lumbarExtensionScore) { this.lumbarExtensionScore = lumbarExtensionScore; }

    public Integer getLumbarExtensionRepetitions(){
        return lumbarExtensionRepetitions;
    }

    public void setLumbarExtensionRepetitions(Integer lumbarExtensionRepetitions) { this.lumbarExtensionRepetitions = lumbarExtensionRepetitions; }

    public Integer getLumbarExtensionWeight(){return lumbarExtensionWeight;}

    public void setLumbarExtensionWeight(Integer lumbarExtensionWeight){this.lumbarExtensionWeight=lumbarExtensionWeight;}

    //Posture
    public Integer getPostureClassification(){ return postureClassification; }

    public void setPostureClassification(Integer postureClassification){ this.postureClassification = postureClassification; }

    public Duration getPostureDuration(){ return postureDuration; }

    public void setPostureDuration(Duration postureDuration){ this.postureDuration = postureDuration; }

    public Duration getCorrectPostureDuration(){ return correctPostureDuration; }

    public void setCorrectPostureDuration(Duration correctPostureDuration){ this.correctPostureDuration = correctPostureDuration; }

    public Duration getWrongPostureDuration(){ return wrongPostureDuration; }

    public void setWrongPostureDuration(Duration wrongPostureDuration){ this.wrongPostureDuration = wrongPostureDuration; }

    //Steps
    public Integer getSteps(){return steps;}

    public void setSteps(Integer steps) {this.steps = steps;}

    //Timestamp
    public Instant getTimestamp(){return timestamp;}

    public void setTimestamp(Instant timestamp){this.timestamp=timestamp;}
}
