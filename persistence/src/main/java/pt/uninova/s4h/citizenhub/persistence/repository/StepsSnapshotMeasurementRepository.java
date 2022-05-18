package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.StepsSnapshotMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.StepsSnapshotMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.WalkingInformation;

public class StepsSnapshotMeasurementRepository {

    private final StepsSnapshotMeasurementDao stepsSnapshotMeasurementDao;

    public StepsSnapshotMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        stepsSnapshotMeasurementDao = citizenHubDatabase.stepsSnapshotMeasurementDao();
    }

    public void add(StepsSnapshotMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> stepsSnapshotMeasurementDao.insert(record));
    }

    public LiveData<List<StepsSnapshotMeasurementRecord>> get(LocalDate localDate) {
        return stepsSnapshotMeasurementDao.get(localDate, localDate.plusDays(1));
    }

    public LiveData<Integer> getMaximum(LocalDate localDate) {
        return stepsSnapshotMeasurementDao.getMaximum(localDate, localDate.plusDays(1));
    }

    public LiveData<WalkingInformation> getLatest(LocalDate localDate) {
        return stepsSnapshotMeasurementDao.getLatest(localDate, localDate.plusDays(1));
    }
}
