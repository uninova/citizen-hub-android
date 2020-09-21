package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import pt.uninova.s4h.citizenhub.connectivity.AbstractProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;

import java.util.UUID;

public class MiBand2AuthenticationProtocol extends AbstractProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.authentication");

    @Override
    public void disable() {

    }

    @Override
    public void enable() {

    }

    @Override
    public UUID getId() {
        return ID;
    }

}
