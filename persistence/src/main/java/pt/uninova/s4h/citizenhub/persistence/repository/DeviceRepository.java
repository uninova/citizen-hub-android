package pt.uninova.s4h.citizenhub.persistence;

import android.content.Context;

import java.util.List;

import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DeviceRepository {

    private final DeviceDao deviceDao;

    public DeviceRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        deviceDao = citizenHubDatabase.deviceDao();
    }

    public void create(DeviceRecord record) {
        CitizenHubDatabase.executorService().execute(() -> deviceDao.insert(record));
    }

    public void delete(String address) {
        CitizenHubDatabase.executorService().execute(() -> deviceDao.delete(address));
    }

    public void delete(DeviceRecord record) {
        CitizenHubDatabase.executorService().execute(() -> deviceDao.delete(record));
    }

    public void deleteAll() {
        CitizenHubDatabase.executorService().execute(deviceDao::deleteAll);
    }

    public void read(String address, Observer<DeviceRecord> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(deviceDao.select(address)));
    }

    public void readAll(Observer<List<DeviceRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(deviceDao.selectAll()));
    }

    public void update(DeviceRecord record) {
        CitizenHubDatabase.executorService().execute(() -> deviceDao.update(record));
    }

    public void updateAgent(String address, String agent) {
        CitizenHubDatabase.executorService().execute(() -> deviceDao.updateAgent(address, agent));
    }
}
