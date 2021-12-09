package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.util.Pair;
import pt.uninova.util.messaging.Observer;
import pt.uninova.util.time.LocalDateInterval;

public class LumbarExtensionTrainingRepository {

    private final LumbarExtensionTrainingDao lumbarExtensionTrainingDao;

    public LumbarExtensionTrainingRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        lumbarExtensionTrainingDao = citizenHubDatabase.lumbarExtensionTrainingDao();
    }


    public void add(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.insert(new LumbarExtensionTraining(lumbarExtensionTraining.getId(),
                    lumbarExtensionTraining.getTimestamp(),
                    lumbarExtensionTraining.getTrainingLength(),
                    lumbarExtensionTraining.getScore(),
                    lumbarExtensionTraining.getRepetitions(),
                    lumbarExtensionTraining.getWeight()));
        });
    }

    public void remove(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.delete(lumbarExtensionTraining);
        });
    }

    public void remove(Integer id, LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.delete(new LumbarExtensionTraining(lumbarExtensionTraining.getId(), lumbarExtensionTraining.getTimestamp(), lumbarExtensionTraining.getTrainingLength(), lumbarExtensionTraining.getScore(), lumbarExtensionTraining.getRepetitions(),lumbarExtensionTraining.getWeight()));
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::deleteAll);
    }

    public void update(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.update(lumbarExtensionTraining));
    }

    public LiveData<LumbarExtensionTraining> getLumbarTraining(LocalDate localDate) {
        return lumbarExtensionTrainingDao.getLumbarTraining(localDate, localDate.plusDays(1));
    }

    public LiveData<LocalDateInterval> getDateBounds() {
        System.out.println(" LUMBAR GET DATE BOUNDS " + lumbarExtensionTrainingDao.getDateBoundsLive().getValue());
        return lumbarExtensionTrainingDao.getDateBoundsLive();
    }

    public void obtainDates(Pair<Integer, Integer> month, Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            final LocalDate from = LocalDate.of(month.getFirst(), month.getSecond(), 1);
            final LocalDate to = LocalDate.of(month.getSecond() == 12 ? month.getFirst() + 1 : month.getFirst(), month.getSecond() == 12 ? 1 : month.getSecond() + 1, 1);
            System.out.println(" LUMBAR OBTAIN DATES " + lumbarExtensionTrainingDao.getDates(from,to));
            observer.onChanged(lumbarExtensionTrainingDao.getDates(from, to));
        });
    }
}