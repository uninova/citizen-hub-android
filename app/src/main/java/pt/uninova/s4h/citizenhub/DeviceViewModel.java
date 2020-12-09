package pt.uninova.s4h.citizenhub;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeviceViewModel extends AndroidViewModel {
    private MutableLiveData<Device> device;
    private LiveData<List<Device>> deviceList;
    final private DeviceRepository deviceRepository;
    private List<Device> pairedDevices;
    private HashSet<String> devices;

    public DeviceViewModel(Application application) {
        super(application);
        deviceRepository = new DeviceRepository(application);
        device = new MutableLiveData<>();
        deviceList = deviceRepository.getAll();
    }

    public LiveData<List<Device>> getDevices() {
        return deviceList;
    }

    public boolean isDevicePaired (String address){
        pairedDevices = deviceList.getValue();
        return pairedDevices.stream().anyMatch(item -> address.equals(item.getAddress()));
    }

    public MutableLiveData<Device> getSelectedDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device.postValue(device);
    }

    public void apply(){
        deviceRepository.add(device.getValue());
    }
    public void delete(Device device){
        deviceRepository.remove(device);
    }
}