package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.DistanceSnapshotMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.DistanceSnapshotMeasurementRecord;

public class DailyDistanceMeasurementRepository {

    private final DistanceSnapshotMeasurementDao distanceSnapshotMeasurementDao;

    public DailyDistanceMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        distanceSnapshotMeasurementDao = citizenHubDatabase.distanceSnapshotMeasurementDao();
    }

    public void add(DistanceSnapshotMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> distanceSnapshotMeasurementDao.insert(record));
    }

    public LiveData<List<DistanceSnapshotMeasurementRecord>> get(LocalDate localDate) {
        return distanceSnapshotMeasurementDao.get(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> getMaximum(LocalDate localDate) {
        return distanceSnapshotMeasurementDao.getMaximum(localDate, localDate.plusDays(1));
    }
}
