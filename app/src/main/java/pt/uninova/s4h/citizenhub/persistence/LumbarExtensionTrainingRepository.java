package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.util.messaging.Observer;

public class LumbarExtensionTrainingRepository {

    private final LumbarExtensionTrainingDao lumbarExtensionTrainingDao;

    public LumbarExtensionTrainingRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        lumbarExtensionTrainingDao = citizenHubDatabase.lumbarExtensionTrainingDao();
    }

    public void create(LumbarExtensionTrainingRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.insert(record));
    }

    public void delete(LumbarExtensionTrainingRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.delete(record));
    }

    public void deleteAll() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::deleteAll);
    }

    public LiveData<List<LumbarExtensionTrainingRecord>> get(LocalDate localDate) {
        return lumbarExtensionTrainingDao.get(localDate, localDate.plusDays(1));
    }

    public void read(LocalDate localDate, Observer<List<LumbarExtensionTrainingRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.select(localDate, localDate.plusDays(1))));
    }

    public void update(LumbarExtensionTrainingRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.update(record));
    }

}