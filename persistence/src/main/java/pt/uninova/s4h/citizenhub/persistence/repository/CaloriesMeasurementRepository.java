package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.CaloriesMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.CaloriesMeasurementRecord;

public class CaloriesMeasurementRepository {

    private final CaloriesMeasurementDao caloriesMeasurementDao;

    public CaloriesMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        caloriesMeasurementDao = citizenHubDatabase.caloriesMeasurementDao();
    }

    public void create(CaloriesMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> caloriesMeasurementDao.insert(record));
    }

    public LiveData<List<CaloriesMeasurementRecord>> read(LocalDate localDate) {
        return caloriesMeasurementDao.selectLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> readMaximum(LocalDate localDate) {
        return caloriesMeasurementDao.selectMaximumLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> getCaloriesAllTypes (LocalDate localDate) {
        return caloriesMeasurementDao.getCaloriesAllTypes(localDate, localDate.plusDays(1));
    }
}