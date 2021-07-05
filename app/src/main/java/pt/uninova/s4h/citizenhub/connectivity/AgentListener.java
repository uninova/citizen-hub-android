package pt.uninova.s4h.citizenhub.connectivity;

import androidx.lifecycle.MutableLiveData;

import pt.uninova.s4h.citizenhub.DeviceListItem;
import pt.uninova.s4h.citizenhub.persistence.Device;

public interface AgentListener {

    MutableLiveData<DeviceListItem> addAgent(Device device);

    boolean OnSuccessConnection(Device device);

    boolean OnFailedConnection(Device device);

}
