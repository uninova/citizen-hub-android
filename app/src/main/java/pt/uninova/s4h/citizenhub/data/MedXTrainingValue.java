package pt.uninova.s4h.citizenhub.data;

import java.time.Duration;
import java.time.Instant;

public class MedXTrainingValue {

    private final Instant timestamp;
    private final Duration duration;
    private final float score;
    private final int repetitions;
    private final int weight;
    private final float calories;

    public MedXTrainingValue(Instant timestamp, Duration duration, float score, int repetitions, int weight, float calories) {
        this.timestamp = timestamp;
        this.duration = duration;
        this.score = score;
        this.repetitions = repetitions;
        this.weight = weight;
        this.calories = calories;
    }

    public float getCalories() {
        return calories;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public float getScore() {
        return score;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getWeight() {
        return weight;
    }
}
