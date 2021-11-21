package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pt.uninova.util.messaging.Observer;

public class FeatureRepository {
    private final FeatureDao featureDao;

    public FeatureRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        featureDao = citizenHubDatabase.featureDao();
    }

    public void add(Feature feature) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.insert(new Feature(feature.getDevice_address(), feature.getKind()));
        });
    }

    public void obtainKindsFromDevice(String address, Observer<List<MeasurementKind>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(featureDao.getKindsFromDevice(address)));
    }


    public LiveData<List<Feature>> getAllLive() {
        return featureDao.getAllLive();
    }

    public void remove(Feature feature) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.delete(feature);
        });
    }

    public void remove(String device_address, MeasurementKind measurementKind) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.delete(new Feature(device_address, measurementKind));
        });
    }

    public void removeAll(String address) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.deleteAll(address);
        });
    }

    public void update(Feature feature) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.update(feature);
        });
    }
}
