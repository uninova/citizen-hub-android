package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Collection;
import java.util.UUID;

public interface Device {

    UUID getId();

    Collection<Source> getSources();

}
