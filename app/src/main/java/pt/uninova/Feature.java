package pt.uninova;

import java.util.UUID;

public interface Feature {

    UUID getId();

    boolean enable();

    boolean disable();


}
