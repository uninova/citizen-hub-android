package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DeviceRepository {
    private final DeviceDao deviceDAO;

    public DeviceRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        deviceDAO = citizenHubDatabase.deviceDao();
    }

    public void add(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.addDevice(device);
        });
    }

    public void update(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.updateDevice(device);
        });
    }

    public void remove(Device device) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.deleteDevice(device);
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDAO.deleteAllDevices();
        });
    }

    public LiveData<List<Device>> getAllDevicesLive() {
        return deviceDAO.getDevices();
    }

    public Device getDevice(String address) {
        return deviceDAO.getDevice(address);
    }
}
