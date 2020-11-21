package pt.uninova.util.time;

import java.time.LocalDate;

public class LocalDateInterval {

    private LocalDate lower;
    private LocalDate upper;

    public LocalDate getLower() {
        return lower;
    }

    public void setLower(LocalDate lower) {
        this.lower = lower;
    }

    public LocalDate getUpper() {
        return upper;
    }

    public void setUpper(LocalDate upper) {
        this.upper = upper;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocalDateInterval)) {
            return false;
        }

        final LocalDateInterval other = (LocalDateInterval) obj;

        return lower.equals(other.lower) && upper.equals(other.upper);
    }
}
