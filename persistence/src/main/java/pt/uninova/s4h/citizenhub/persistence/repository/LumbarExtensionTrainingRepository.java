package pt.uninova.s4h.citizenhub.persistence.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.LumbarExtensionTrainingDao;
import pt.uninova.s4h.citizenhub.persistence.entity.LumbarExtensionTrainingMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.LumbarExtensionTrainingSummary;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class LumbarExtensionTrainingRepository {

    private final LumbarExtensionTrainingDao lumbarExtensionTrainingDao;

    public LumbarExtensionTrainingRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        lumbarExtensionTrainingDao = citizenHubDatabase.lumbarExtensionTrainingDao();
    }

    public void create(LumbarExtensionTrainingMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.insert(record));
    }

    public void delete(LumbarExtensionTrainingMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.delete(record));
    }

    public void deleteAll() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::deleteAll);
    }

    public LiveData<List<LumbarExtensionTrainingMeasurementRecord>> get(LocalDate localDate) {
        return lumbarExtensionTrainingDao.get(localDate, localDate.plusDays(1));
    }

    public LiveData<LumbarExtensionTrainingSummary> getLatest(LocalDate localDate) {
        return lumbarExtensionTrainingDao.getLatest(localDate, localDate.plusDays(1));
    }

    public void read(LocalDate localDate, Observer<List<LumbarExtensionTrainingMeasurementRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.select(localDate, localDate.plusDays(1))));
    }

    public void update(LumbarExtensionTrainingMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.update(record));
    }

}