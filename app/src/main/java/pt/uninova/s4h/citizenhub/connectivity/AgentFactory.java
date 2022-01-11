package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.messaging.Observer;

public interface AgentFactory<T extends Agent> {

    void create(String address, Observer<T> observer);

}
