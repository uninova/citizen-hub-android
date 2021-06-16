package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Objects;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubServiceBound;

public class DeviceViewModel extends AndroidViewModel {
    private final MutableLiveData<Device> device;
    private final LiveData<List<Device>> deviceList;
    final private DeviceRepository deviceRepository;

    public DeviceViewModel(Application application) {
        super(application);
        deviceRepository = new DeviceRepository(application);
        device = new MutableLiveData<>();
        deviceList = deviceRepository.getAll();
    }

    public LiveData<List<Device>> getDevices() {
        return deviceList;
    }

    public boolean isDevicePaired(Device device) {
        List<Device> pairedDevices = deviceList.getValue();
        if (pairedDevices != null) {
            return deviceList.getValue().contains(device);
        }
        return false;
    }

    public MutableLiveData<Device> getSelectedDevice() {
        return device;
    }

    public Agent getSelectedAgent() {
        Agent agent = ((CitizenHubServiceBound) getApplication()).getService().getAgentOrchestrator().getDeviceAgentMap().get(device.getValue());
        if (agent == null) throw new NullPointerException();
        return agent;
    }

    public void setDevice(Device device) {
        this.device.postValue(device);
    }

    public void getDeviceFeatures(Device device) {
        ((CitizenHubServiceBound) getApplication()).getService().getAgentOrchestrator().getDeviceAgentMap().get(device).getSupportedMeasurements();
    }

    public void enableDeviceFeature(MeasurementKind measurementKind) {
        Objects.requireNonNull(((CitizenHubServiceBound) getApplication()).getService().getAgentOrchestrator().getDeviceAgentMap().get(device.getValue())).enableMeasurement(measurementKind);
    }

    public void apply() {
        deviceRepository.add(device.getValue());
    }

    public void delete(Device device) {
        deviceRepository.remove(device);
    }
}