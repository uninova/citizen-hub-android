package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.FeatureDao;
import pt.uninova.s4h.citizenhub.persistence.entity.Feature;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class FeatureRepository {
    private final FeatureDao featureDao;

    public FeatureRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);
        featureDao = citizenHubDatabase.featureDao();
    }

    public void add(Feature feature) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.insert(new Feature(feature.getDevice_address(), feature.getKind()));
        });
    }

    public void obtainKindsFromDevice(String address, Observer<List<Integer>> observer) {
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

    public void remove(String device_address, Integer measurementKind) {
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
