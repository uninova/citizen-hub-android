package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {
    private final LiveData<List<Device>> deviceList;

    public DeviceViewModel(@NonNull Application application) {
        super(application);
        final DeviceRepository deviceRepository = new DeviceRepository(application);
        deviceList = deviceRepository.getAllDevicesLive();
    }

    public LiveData<List<Device>> getAllDevicesLive() {
        return deviceList;
    }

}