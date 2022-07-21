package pt.uninova.s4h.citizenhub.report;

import java.time.Instant;

public class IsoTimestampLocalizedResource extends TimestampLocalizedResource {

    public IsoTimestampLocalizedResource(Instant timestamp) {
        super(timestamp);
    }

    @Override
    public String getLocalizedString() {
        return getTimestamp().toString();
    }
}
