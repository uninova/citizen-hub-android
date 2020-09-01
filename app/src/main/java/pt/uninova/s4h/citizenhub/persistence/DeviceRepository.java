package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;
import pt.uninova.util.Observer;

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

    public Device get(String address) {
        return deviceDao.get(address);
    }

    public LiveData<List<Device>> getAll() {
        return deviceDao.getAll();
    }

    public void obtain(String address, Observer<Device> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChange(deviceDao.get(address)));
    }

    public void remove(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.delete(device);
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(deviceDao::deleteAll);
    }

    public void update(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.update(device);
        });
    }
}
