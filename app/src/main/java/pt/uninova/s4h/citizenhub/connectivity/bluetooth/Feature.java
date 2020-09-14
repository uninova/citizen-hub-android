package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public interface Feature {

    UUID getId();

    boolean enable();

    boolean disable();


}
