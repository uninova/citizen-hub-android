package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.EnabledMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.EnabledMeasurementRecord;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class EnabledMeasurementRepository {

    private final EnabledMeasurementDao enabledMeasurementDao;

    public EnabledMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        enabledMeasurementDao = citizenHubDatabase.enabledMeasurementDao();
    }

    public void create(EnabledMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> enabledMeasurementDao.insert(record));
    }

    public void create(String deviceAddress, Integer measurementType) {
        CitizenHubDatabase.executorService().execute(() -> enabledMeasurementDao.insert(deviceAddress, measurementType));
    }

    public void delete(EnabledMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> enabledMeasurementDao.delete(record));
    }

    public void delete(Long deviceId) {
        CitizenHubDatabase.executorService().execute(() -> enabledMeasurementDao.delete(deviceId));
    }

    public void delete(Long deviceId, Integer measurementType) {
        delete(new EnabledMeasurementRecord(deviceId, measurementType));
    }

    public void delete(String deviceAddress, Integer measurementType) {
        CitizenHubDatabase.executorService().execute(() -> enabledMeasurementDao.delete(deviceAddress, measurementType));
    }

    public LiveData<List<EnabledMeasurementRecord>> read() {
        return enabledMeasurementDao.selectLiveData();
    }

    public void read(Long deviceId, Observer<List<EnabledMeasurementRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(enabledMeasurementDao.select(deviceId)));
    }

    public void read(String address, Observer<List<EnabledMeasurementRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(enabledMeasurementDao.select(address)));
    }

    public void update(EnabledMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> enabledMeasurementDao.update(record));
    }
}