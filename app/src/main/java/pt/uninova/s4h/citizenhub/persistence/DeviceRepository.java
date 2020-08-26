package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class DeviceRepository {
    private final DeviceDao deviceDAO;

    public DeviceRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        deviceDAO = citizenHubDatabase.deviceDao();
    }

    public void add(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.insert(device);
        });
    }

    public void update(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.update(device);
        });
    }

    public void remove(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.delete(device);
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.deleteAll();
        });
    }

    public LiveData<List<Device>> getAll() {
        return deviceDAO.getAll();
    }

    public Device get(String address) {
        return deviceDAO.get(address);
    }

    public void obtain(String address, Observer<Device> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(deviceDAO.get(address)));
    }
}
