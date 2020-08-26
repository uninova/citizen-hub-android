package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class DeviceRepository {
    private final DeviceDao deviceDao;

    public DeviceRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        deviceDao = citizenHubDatabase.deviceDao();
    }

    public void add(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.insert(device);
        });
    }

    public void update(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.update(device);
        });
    }

    public void remove(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.delete(device);
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.deleteAll();
        });
    }

    public LiveData<List<Device>> getAll() {
        return deviceDao.getAll();
    }

    public Device get(String address) {
        return deviceDao.get(address);
    }

    public void obtain(String address, Observer<Device> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(deviceDao.get(address)));
    }
}
