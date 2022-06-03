package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.FeatureDao;
import pt.uninova.s4h.citizenhub.persistence.entity.DeviceEnabledMeasurement;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class FeatureRepository {
    private final FeatureDao featureDao;

    public FeatureRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);
        featureDao = citizenHubDatabase.featureDao();
    }

    public void add(DeviceEnabledMeasurement deviceEnabledMeasurement) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.insert(new DeviceEnabledMeasurement(deviceEnabledMeasurement.getDevice_address(), deviceEnabledMeasurement.getKind()));
        });
    }

    public void obtainKindsFromDevice(String address, Observer<List<Integer>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(featureDao.getKindsFromDevice(address)));
    }


    public LiveData<List<DeviceEnabledMeasurement>> getAllLive() {
        return featureDao.getAllLive();
    }

    public void remove(DeviceEnabledMeasurement deviceEnabledMeasurement) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.delete(deviceEnabledMeasurement);
        });
    }

    public void remove(String device_address, Integer measurementKind) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.delete(new DeviceEnabledMeasurement(device_address, measurementKind));
        });
    }

    public void removeAll(String address) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.deleteAll(address);
        });
    }

    public void update(DeviceEnabledMeasurement deviceEnabledMeasurement) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.update(deviceEnabledMeasurement);
        });
    }
}
