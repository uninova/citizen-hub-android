package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class LumbarExtensionTrainingRepository {

    private final LumbarExtensionTrainingDao lumbarExtensionTrainingDao;

    private final Map<LocalDate, LiveData<Map<MeasurementKind, MeasurementAggregate>>> dailyAggregateMap;

    public LumbarExtensionTrainingRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        lumbarExtensionTrainingDao = citizenHubDatabase.lumbarExtensionTrainingDao();
        dailyAggregateMap = new HashMap<>();
    }


    public void add(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.insert(new LumbarExtensionTraining(lumbarExtensionTraining.getId(), lumbarExtensionTraining.getTimestamp(), lumbarExtensionTraining.getRepetitions(), lumbarExtensionTraining.getTrainingLength(), lumbarExtensionTraining.getScore()));
        });
    }

    public void remove(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.delete(lumbarExtensionTraining);
        });
    }

    public void remove(Integer id, LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.delete(new LumbarExtensionTraining(lumbarExtensionTraining.getId(), lumbarExtensionTraining.getTimestamp(), lumbarExtensionTraining.getRepetitions(), lumbarExtensionTraining.getTrainingLength(), lumbarExtensionTraining.getScore()));
        });
    }

    public void getAll() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::deleteAll);
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::deleteAll);
    }

    public void update(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.update(lumbarExtensionTraining));
    }

}