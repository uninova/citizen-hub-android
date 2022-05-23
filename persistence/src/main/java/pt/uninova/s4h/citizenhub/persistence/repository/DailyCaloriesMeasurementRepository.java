package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.CaloriesSnapshotMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.CaloriesSnapshotMeasurementRecord;

public class DailyCaloriesMeasurementRepository {

    private final CaloriesSnapshotMeasurementDao caloriesSnapshotMeasurementDao;

    public DailyCaloriesMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        caloriesSnapshotMeasurementDao = citizenHubDatabase.caloriesSnapshotMeasurementDao();
    }

    public void add(CaloriesSnapshotMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> caloriesSnapshotMeasurementDao.insert(record));
    }

    public LiveData<List<CaloriesSnapshotMeasurementRecord>> get(LocalDate localDate) {
        return caloriesSnapshotMeasurementDao.get(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> getMaximum(LocalDate localDate) {
        return caloriesSnapshotMeasurementDao.getMaximum(localDate, localDate.plusDays(1));
    }
}
