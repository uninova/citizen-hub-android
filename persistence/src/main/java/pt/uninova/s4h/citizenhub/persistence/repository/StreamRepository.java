package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.StreamDao;
import pt.uninova.s4h.citizenhub.persistence.entity.StreamRecord;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class StreamRepository {

    private final StreamDao streamDao;

    public StreamRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        streamDao = citizenHubDatabase.enabledMeasurementDao();
    }

    public void create(StreamRecord record) {
        CitizenHubDatabase.executorService().execute(() -> streamDao.insert(record));
    }

    public void create(String deviceAddress, Integer measurementType) {
        CitizenHubDatabase.executorService().execute(() -> streamDao.insert(deviceAddress, measurementType));
    }

    public void delete(StreamRecord record) {
        CitizenHubDatabase.executorService().execute(() -> streamDao.delete(record));
    }

    public void delete(Long deviceId) {
        CitizenHubDatabase.executorService().execute(() -> streamDao.delete(deviceId));
    }

    public void delete(Long deviceId, Integer measurementType) {
        delete(new StreamRecord(deviceId, measurementType));
    }

    public void delete(String deviceAddress, Integer measurementType) {
        CitizenHubDatabase.executorService().execute(() -> streamDao.delete(deviceAddress, measurementType));
    }

    public LiveData<List<StreamRecord>> read() {
        return streamDao.selectLiveData();
    }

    public void read(Long deviceId, Observer<List<StreamRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(streamDao.select(deviceId)));
    }

    public void read(String address, Observer<List<StreamRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(streamDao.select(address)));
    }

    public void update(StreamRecord record) {
        CitizenHubDatabase.executorService().execute(() -> streamDao.update(record));
    }
}