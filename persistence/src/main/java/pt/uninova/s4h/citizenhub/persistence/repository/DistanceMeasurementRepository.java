package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.DistanceMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.DistanceMeasurementRecord;

public class DistanceMeasurementRepository {

    private final DistanceMeasurementDao distanceMeasurementDao;

    public DistanceMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        distanceMeasurementDao = citizenHubDatabase.distanceMeasurementDao();
    }

    public void create(DistanceMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> distanceMeasurementDao.insert(record));
    }

    public LiveData<List<DistanceMeasurementRecord>> read(LocalDate localDate) {
        return distanceMeasurementDao.selectLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> readMaximum(LocalDate localDate) {
        return distanceMeasurementDao.selectMaximumLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> getDistanceAllTypes (LocalDate localDate) {
        return distanceMeasurementDao.getDistanceAllTypes(localDate, localDate.plusDays(1));
    }
}