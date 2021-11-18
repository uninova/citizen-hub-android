package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MedExRepository {

    private final MedExDao medExDao;

    private final Map<LocalDate, LiveData<Map<MeasurementKind, MeasurementAggregate>>> dailyAggregateMap;

    public MedExRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        medExDao = citizenHubDatabase.medExDao();
        dailyAggregateMap = new HashMap<>();
    }


    public void add(MedEx medEx) {
        CitizenHubDatabase.executorService().execute(() -> {
            medExDao.insert(new MedEx(medEx.getId(), medEx.getTimestamp(), medEx.getRepetitions(), medEx.getTrainingLength(), medEx.getScore()));
        });
    }

    public void remove(MedEx medEx) {
        CitizenHubDatabase.executorService().execute(() -> {
            medExDao.delete(medEx);
        });
    }

    public void remove(Integer id, MedEx medEx) {
        CitizenHubDatabase.executorService().execute(() -> {
            medExDao.delete(new MedEx(medEx.getId(), medEx.getTimestamp(), medEx.getRepetitions(), medEx.getTrainingLength(), medEx.getScore()));
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(medExDao::deleteAll);
    }

    public void update(MedEx medEx) {
        CitizenHubDatabase.executorService().execute(() -> medExDao.update(medEx));
    }

}