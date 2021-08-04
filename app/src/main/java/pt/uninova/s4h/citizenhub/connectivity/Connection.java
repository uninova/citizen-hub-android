package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;

public interface Connection {

    ConnectionKind getConnectionKind();

    String getAddress();

}
