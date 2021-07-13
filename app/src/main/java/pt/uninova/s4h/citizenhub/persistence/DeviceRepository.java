package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import java.util.List;

import pt.uninova.util.messaging.Observer;

import static pt.uninova.s4h.citizenhub.persistence.AgentStateAnnotation.StateAnnotation;

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
        try {
            return deviceDao.get(address);
        } catch (Exception e) {
            return null;
        }
    }


    public void obtain(String address, Observer<Device> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(deviceDao.get(address)));
    }

    public void obtainAll(Observer<List<Device>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.onChanged(deviceDao.getAll()));
    }

    public List<Device> getAllWithConnectionKind(ConnectionKind connectionKind) {
        try {
            return deviceDao.getAllWithConnectionKind(connectionKind);
        } catch (Exception e) {
            return null;
        }
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
        //mudar o state
        CitizenHubDatabase.executorService().execute(() -> {
            deviceDao.update(device);
        });
    }

    public List<Device> getWithState(StateAnnotation state) {
        try {
            return deviceDao.getWithState(state);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Device> getWithAgent(String type) {
        try {
            return deviceDao.getWithAgent(type);
        } catch (Exception e) {
            return null;
        }
    }
}
