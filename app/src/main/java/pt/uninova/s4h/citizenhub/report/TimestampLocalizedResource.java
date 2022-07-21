package pt.uninova.s4h.citizenhub.report;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class TimestampLocalizedResource implements LocalizedResource {

    private final Instant timestamp;

    public TimestampLocalizedResource(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getLocalizedString() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        return formatter.format(timestamp.atZone(ZoneId.systemDefault()));
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
