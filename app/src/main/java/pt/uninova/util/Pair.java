package pt.uninova.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Pair<F, S> {

    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }

        final Pair<?, ?> other = (Pair<?, ?>) obj;

        return first.equals(other.first) && second.equals(other.second);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + first + "," + second + ")";
    }
}
