package pt.uninova.s4h.citizenhub;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class AgentListEvent {

    private final List<Device> devices;

    public AgentListEvent(List<Device> deviceList) {
        this.devices = deviceList;

    }

    public List<Device> getDevices() {
        return devices;
    }
}
