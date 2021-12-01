package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

   public LumbarExtensionTraining getLumbarTraining(LocalDate localDate){
       return lumbarExtensionTrainingDao.getLumbarTraining( localDate, localDate.plusDays(1));
   }
}