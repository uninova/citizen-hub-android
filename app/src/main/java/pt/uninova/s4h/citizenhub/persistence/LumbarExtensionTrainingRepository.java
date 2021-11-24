package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uninova.util.Pair;
import pt.uninova.util.messaging.Observer;

public class LumbarExtensionTrainingRepository {

    private final LumbarExtensionTrainingDao lumbarExtensionTrainingDao;

    private final Map<LocalDate, LiveData<Map<MeasurementKind, LumbarAggregate>>> lumbarAggregateMap;

    public LumbarExtensionTrainingRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        lumbarExtensionTrainingDao = citizenHubDatabase.lumbarExtensionTrainingDao();
        lumbarAggregateMap = new HashMap<>();
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

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::deleteAll);
    }

    public void update(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.update(lumbarExtensionTraining));
    }

    private Map<MeasurementKind, LumbarAggregate> mapAggregates(List<LumbarAggregate> aggregates) {
        final Map<MeasurementKind, LumbarAggregate> aggregateMap = new HashMap<>(aggregates.size());

        for (LumbarAggregate i : aggregates) {
            aggregateMap.put(i.getMeasurementKind(), i);
        }

        return aggregateMap;
    }

    public LiveData<Map<MeasurementKind, LumbarAggregate>> getCurrentDailyAggregate() {
        return getDailyAggregate(LocalDate.now());
    }

    public LiveData<Map<MeasurementKind, LumbarAggregate>> getDailyAggregate(LocalDate localDate) {
        System.out.println();
        if (lumbarAggregateMap.containsKey(localDate)) {
            return lumbarAggregateMap.get(localDate);
        } else {
            final MediatorLiveData<Map<MeasurementKind, LumbarAggregate>> data = new MediatorLiveData<>();

            lumbarAggregateMap.put(localDate, data);

            data.addSource(lumbarExtensionTrainingDao.getAggregate(localDate,localDate.plusDays(1)), aggregates -> {
                data.postValue(mapAggregates(aggregates));
            });


            return data;
        }
    }




}