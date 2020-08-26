package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

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