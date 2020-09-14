package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FeatureRepository {
    private final FeatureDao featureDao;

    public FeatureRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        featureDao = citizenHubDatabase.featureDao();
    }

    public void add(Feature feature) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.insert(feature);
        });
    }

    public List<Feature> getAll(String address) {
        return featureDao.getAll(address);
    }

    public List<Feature> getAllSpecific(String feature_address) {
        return featureDao.getAll(feature_address);
    }

    public Feature get(String uuid) {
        return featureDao.get(uuid);
    }

    public LiveData<List<Feature>> getAllLive() {
        return featureDao.getAllLive();
    }

    public void remove(Feature feature) {
        CitizenHubDatabase.executorService().execute(() -> {
            featureDao.delete(feature);
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
