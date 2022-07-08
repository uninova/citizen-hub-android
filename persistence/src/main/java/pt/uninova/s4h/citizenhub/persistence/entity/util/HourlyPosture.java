package pt.uninova.s4h.citizenhub.persistence.entity.util;

import java.time.Duration;

public class HourlyPosture {

    private final Integer hour;
    private final Duration correctPostureDuration;
    private final Duration incorrectPostureDuration;

    public HourlyPosture(Integer hour, Duration correctPostureDuration, Duration incorrectPostureDuration) {
        this.hour = hour;
        this.correctPostureDuration = correctPostureDuration;
        this.incorrectPostureDuration = incorrectPostureDuration;
    }

    public Duration getCorrectPostureDuration() {
        return correctPostureDuration;
    }

    public Integer getHour() {
        return hour;
    }

    public Duration getIncorrectPostureDuration() {
        return incorrectPostureDuration;
    }
}
