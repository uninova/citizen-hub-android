package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import java.util.List;

import pt.uninova.util.messaging.Observer;

public class DeviceRepository {

    private final DeviceDao deviceDao;

    public DeviceRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        deviceDao = citizenHubDatabase.deviceDao();
    }

    public void add(DeviceRecord deviceRecord) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.insert(deviceRecord);
        });
    }

    public DeviceRecord get(String address) {
        try {
            return deviceDao.get(address);
        } catch (Exception e) {
            return null;
        }
    }


    public void obtain(String address, Observer<DeviceRecord> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(deviceDao.get(address)));
    }

    public void obtainAll(Observer<List<DeviceRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(deviceDao.getAll()));
    }

    public List<DeviceRecord> getAllWithConnectionKind(ConnectionKind connectionKind) {
        try {
            return deviceDao.getAllWithConnectionKind(connectionKind);
        } catch (Exception e) {
            return null;
        }
    }

    public void remove(DeviceRecord deviceRecord) {
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.delete(deviceRecord);
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(deviceDao::deleteAll);
    }

    public void update(DeviceRecord deviceRecord) {
        //mudar o state
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.update(deviceRecord);
        });
    }

    public List<DeviceRecord> getWithState(StateKind state) {
        try {
            return deviceDao.getWithState(state);
        } catch (Exception e) {
            return null;
        }
    }

    public List<DeviceRecord> getWithAgent(String type) {
        try {
            return deviceDao.getWithAgent(type);
        } catch (Exception e) {
            return null;
        }
    }
}
