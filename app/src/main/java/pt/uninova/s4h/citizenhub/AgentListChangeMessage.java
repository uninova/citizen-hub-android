package pt.uninova.s4h.citizenhub;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class AgentListChangeMessage<A> {

    final private List<Device> deviceList;

    public AgentListChangeMessage(List<Device> devices) {
        this.deviceList = devices;
    }

    public <A> List<Device> getDeviceList() {
        return deviceList;
    }
}