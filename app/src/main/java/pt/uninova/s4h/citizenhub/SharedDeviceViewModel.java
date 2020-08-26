package pt.uninova.s4h.citizenhub;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;

public class SharedDeviceViewModel extends ViewModel {
    private MutableLiveData<Device> device;
    private DeviceRepository deviceRepository;

    public SharedDeviceViewModel(Application application) {
         DeviceRepository deviceRepository = new DeviceRepository(application);
    }

    public LiveData<List<Device>> getAllDevicesLive() {
        return deviceRepository.getAllDevicesLive();
    }

    public MutableLiveData<Device> getDevice() {
        return device;
    }

    public void setDevice(MutableLiveData<Device> device) {
        this.device = device;
    }
}