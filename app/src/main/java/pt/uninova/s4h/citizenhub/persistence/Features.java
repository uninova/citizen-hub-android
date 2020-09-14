package pt.uninova.s4h.citizenhub.persistence;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.Feature;

public class Features implements Feature {

    private UUID uuid;

    public Features(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    @Override
    public UUID getId() {
        return uuid;
    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public boolean disable() {
        return false;
    }
}
