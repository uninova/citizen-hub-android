package pt.uninova.s4h.citizenhub.ui.devices;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestratorListener;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;

public class DeviceViewModel extends AndroidViewModel {

    private final ServiceConnection serviceConnection;
    private final AgentOrchestratorListener agentOrchestratorListener;

    private final MutableLiveData<AgentOrchestrator> agentOrchestratorLiveData;
    private final MutableLiveData<List<Device>> deviceListLiveData;
    private final MutableLiveData<Device> selectedDeviceLiveData;

    public DeviceViewModel(Application application) {
        super(application);

        agentOrchestratorLiveData = new MutableLiveData<>();
        deviceListLiveData = new MutableLiveData<>(Collections.emptyList());
        selectedDeviceLiveData = new MutableLiveData<>();

        serviceConnection = new ServiceConnection() {

            private CitizenHubService service;

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                service = ((CitizenHubService.Binder) iBinder).getService();

                final AgentOrchestrator agentOrchestrator = service.getAgentOrchestrator();

                agentOrchestrator.addListener(agentOrchestratorListener);

                final List<Device> deviceList = new ArrayList<>(agentOrchestrator.getDevices());

                Collections.sort(deviceList);

                agentOrchestratorLiveData.postValue(agentOrchestrator);
                deviceListLiveData.postValue(deviceList);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                service.getAgentOrchestrator().removeListener(agentOrchestratorListener);
                agentOrchestratorLiveData.postValue(null);
            }
        };

        agentOrchestratorListener = new AgentOrchestratorListener() {
            @Override
            public void onDeviceAdded(Device device) {
                final List<Device> deviceList = deviceListLiveData.getValue();

                deviceList.add(device);
                Collections.sort(deviceList);
                deviceListLiveData.postValue(deviceList);
            }

            @Override
            public void onDeviceRemoved(Device device) {
                final List<Device> deviceList = deviceListLiveData.getValue();

                deviceList.remove(device);
                deviceListLiveData.postValue(deviceList);
            }
        };

        CitizenHubService.bind(application, serviceConnection);
    }

    public LiveData<AgentOrchestrator> getAgentOrchestrator() {
        return agentOrchestratorLiveData;
    }

    public LiveData<List<Device>> getDeviceList() {
        return deviceListLiveData;
    }

    public LiveData<Device> getSelectedDevice() {
        return selectedDeviceLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        CitizenHubService.unbind(getApplication(), serviceConnection);
    }

    public void selectDevice(Device device) {
        selectedDeviceLiveData.postValue(device);
    }
}
