package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.persistence.Device;

public interface AgentListener {

    void OnConnectingDevice(Device device);

    boolean OnSuccessConnection(Device device);

    boolean OnFailedConnection(Device device);

}
