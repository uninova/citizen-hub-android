package pt.uninova.s4h.citizenhub;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {
    private MutableLiveData<Device> device = new MutableLiveData<Device>();
    private LiveData<List<Device>> deviceList;

    public DeviceViewModel(Application application) {
        super(application);
        final DeviceRepository deviceRepository = new DeviceRepository(application);
        device = new MutableLiveData<>();
        deviceList = deviceRepository.getAll();

    }

    public LiveData<List<Device>> getDevices() {
        return deviceList;
    }

    public MutableLiveData<Device> getSelectedDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device.postValue(device);
    }
}