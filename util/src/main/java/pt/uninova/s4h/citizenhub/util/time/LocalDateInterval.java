package pt.uninova.s4h.citizenhub.util.time;

import java.time.LocalDate;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalDateInterval that = (LocalDateInterval) o;
        return Objects.equals(lower, that.lower) && Objects.equals(upper, that.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }
}
