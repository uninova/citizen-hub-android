package pt.uninova.s4h.citizenhub.data;

import java.time.Instant;

import pt.uninova.s4h.citizenhub.connectivity.Device;

public class Sample {

    private final Instant timestamp;
    private final Device source;
    private final Measurement<?>[] measurements;

    public Sample(Device source, Measurement<?>... measurements) {
        this(Instant.now(), source, measurements);
    }

    public Sample(Instant timestamp, Device source, Measurement<?>... measurements) {
        this.timestamp = timestamp;
        this.source = source;
        this.measurements = measurements;
    }

    public Device getSource() {
        return source;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Measurement<?>[] getMeasurements() {
        return measurements;
    }
}
