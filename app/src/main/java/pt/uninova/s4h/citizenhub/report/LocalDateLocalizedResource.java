package pt.uninova.s4h.citizenhub.report;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class LocalDateLocalizedResource implements LocalizedResource {

    private final LocalDate localDate;

    public LocalDateLocalizedResource(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDateLocalizedResource(Instant timestamp) {
        this.localDate = timestamp.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public String getLocalizedString() {
        return localDate.toString();
    }
}
