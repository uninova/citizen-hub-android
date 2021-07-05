package pt.uninova.s4h.citizenhub.connectivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import pt.uninova.s4h.citizenhub.DeviceListItem;
import pt.uninova.s4h.citizenhub.persistence.Device;

public class AgentNotification {

    public LiveData<DeviceListItem> agentAddEvent(Device device) {
        if (device != null)
            System.out.println("AGENT NOTIFICATION" + " " + device.getAddress() + " - " + device.getName());
        MutableLiveData<DeviceListItem> result = new MutableLiveData<>();
        result.postValue(new DeviceListItem(device));
        return result;
    }

    public LiveData<DeviceListItem> agentRemoveEvent(Device device) {
        MutableLiveData<DeviceListItem> result = new MutableLiveData<>();
        result.postValue(new DeviceListItem(device));
        return result;
    }
}