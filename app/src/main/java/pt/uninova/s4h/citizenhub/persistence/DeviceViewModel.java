package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//ViewModel outlives activities, that's why we pass application instead of context so there are no memory leaks

public class DeviceViewModel extends AndroidViewModel {
    private DeviceRepository repository;
    private LiveData<List<Device>> allDevices;

    public DeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(application);
        allDevices = repository.getAllDevices();
    }

    public void insert(Device device) {
        repository.insert(device);
    }

    public void update(Device device) {
        repository.update(device);
    }

    public void delete(Device device) {
        repository.delete(device);
    }

    public void deleteAllDevices() {
        repository.deleteAll();
    }

    public LiveData<List<Device>> getAllDevices() {
        return allDevices;
    }

}