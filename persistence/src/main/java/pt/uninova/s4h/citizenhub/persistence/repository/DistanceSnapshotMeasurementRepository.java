package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.DistanceSnapshotMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.DistanceSnapshotMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DistanceSnapshotMeasurementRepository {

    private final DistanceSnapshotMeasurementDao distanceSnapshotMeasurementDao;

    public DistanceSnapshotMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        distanceSnapshotMeasurementDao = citizenHubDatabase.distanceSnapshotMeasurementDao();
    }

    public void create(DistanceSnapshotMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> distanceSnapshotMeasurementDao.insert(record));
    }

    public LiveData<List<DistanceSnapshotMeasurementRecord>> read(LocalDate localDate) {
        return distanceSnapshotMeasurementDao.selectLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> readMaximum(LocalDate localDate) {
        return distanceSnapshotMeasurementDao.selectMaximumLiveData(localDate, localDate.plusDays(1));
    }

    public void readLastDay(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(distanceSnapshotMeasurementDao.selectLastDay(localDate)));
    }

    public void readLastSevenDays(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(distanceSnapshotMeasurementDao.selectLastSevenDays(localDate.minusDays(7), localDate)));
    }

    public void readLastThirtyDays(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(distanceSnapshotMeasurementDao.selectLastThirtyDays(localDate.minusDays(30), localDate)));
    }

}