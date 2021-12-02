package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import java.time.LocalDate;

public class LumbarExtensionTrainingRepository {

    private final LumbarExtensionTrainingDao lumbarExtensionTrainingDao;

    public LumbarExtensionTrainingRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        lumbarExtensionTrainingDao = citizenHubDatabase.lumbarExtensionTrainingDao();
    }


    public void add(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.insert(new LumbarExtensionTraining(lumbarExtensionTraining.getId(), lumbarExtensionTraining.getTimestamp(), lumbarExtensionTraining.getTrainingLength(), lumbarExtensionTraining.getScore(), lumbarExtensionTraining.getRepetitions()));
        });
    }

    public void remove(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.delete(lumbarExtensionTraining);
        });
    }

    public void remove(Integer id, LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> {
            lumbarExtensionTrainingDao.delete(new LumbarExtensionTraining(lumbarExtensionTraining.getId(), lumbarExtensionTraining.getTimestamp(), lumbarExtensionTraining.getTrainingLength(), lumbarExtensionTraining.getScore(), lumbarExtensionTraining.getRepetitions()));
        });
    }

    public void removeAll() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::deleteAll);
    }

    public void update(LumbarExtensionTraining lumbarExtensionTraining) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.update(lumbarExtensionTraining));
    }

    public LumbarExtensionTraining getLumbarTraining(LocalDate localDate) {
        try {
            return lumbarExtensionTrainingDao.getLumbarTraining(localDate, localDate.plusDays(1));

        } catch (Exception e) {
            return null;
        }
    }
}