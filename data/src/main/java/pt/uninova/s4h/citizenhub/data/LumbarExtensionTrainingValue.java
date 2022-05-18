package pt.uninova.s4h.citizenhub.data;

import java.time.Duration;

public class LumbarExtensionTrainingValue {

    private final Duration duration;
    private final Double score;
    private final Integer repetitions;
    private final Integer weight;

    public LumbarExtensionTrainingValue(Duration duration, Double score, Integer repetitions, Integer weight) {
        this.duration = duration;
        this.score = score;
        this.repetitions = repetitions;
        this.weight = weight;
    }

    public Duration getDuration() {
        return duration;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public Double getScore() {
        return score;
    }

    public Integer getWeight() {
        return weight;
    }
}
