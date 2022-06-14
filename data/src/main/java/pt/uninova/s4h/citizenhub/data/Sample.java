package pt.uninova.s4h.citizenhub.data;

import java.time.Instant;
import java.util.List;

public class Sample {

    private final Instant timestamp;
    private final Device source;
    private final Measurement<?>[] measurements;

    public Sample(Device source, List<Measurement<?>> measurements) {
        this(Instant.now(), source, measurements);
    }

    public Sample(Device source, Measurement<?>... measurements) {
        this(Instant.now(), source, measurements);
    }

    public Sample(Instant timestamp, Device source, List<Measurement<?>> measurements) {
        this.timestamp = timestamp;
        this.source = source;
        this.measurements = new Measurement<?>[measurements.size()];

        int offset = 0;

        for (Measurement<?> i : measurements) {
            this.measurements[offset++] = i;
        }
    }

    public Sample(Instant timestamp, Device source, Measurement<?>... measurements) {
        this.timestamp = timestamp;
        this.source = source;
        this.measurements = measurements;
    }

    public Measurement<?>[] getMeasurements() {
        return measurements;
    }

    public Device getSource() {
        return source;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
