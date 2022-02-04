package pt.uninova.s4h.citizenhub.data;

import java.time.Duration;
import java.time.Instant;

public class MedXTrainingValue {

    private final Instant timestamp;
    private final Duration duration;
    private final float score;
    private final int repetitions;
    private final int weight;

    public MedXTrainingValue(Instant timestamp, Duration duration, float score, int repetitions, int weight) {
        this.timestamp = timestamp;
        this.duration = duration;
        this.score = score;
        this.repetitions = repetitions;
        this.weight = weight;
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
