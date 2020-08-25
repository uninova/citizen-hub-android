package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {
    private final LiveData<List<Device>> allDevices;
    final DeviceRepository deviceRepository;
    public DeviceViewModel(@NonNull Application application) {
        super(application);
        deviceRepository = new DeviceRepository(application);
        allDevices = deviceRepository.getAllDevices();
    }

    public void insert(Device device) {
        deviceRepository.insert(device);
    }

    public void update(Device device) {
        deviceRepository.update(device);
    }

    public void delete(Device device) {
        deviceRepository.delete(device);
    }

    public void deleteAllDevices() {
        deviceRepository.deleteAll();
    }

    public LiveData<List<Device>> getAllDevices() {
        return allDevices;
    }

}