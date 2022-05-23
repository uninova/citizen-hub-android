package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.PulseRateMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.PulseRateMeasurementRecord;

public class PulseRateMeasurementRepository {

    private final PulseRateMeasurementDao pulseRateMeasurementDao;

    public PulseRateMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        pulseRateMeasurementDao = citizenHubDatabase.pulseRateMeasurementDao();
    }

    public void add(PulseRateMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> pulseRateMeasurementDao.insert(record));
    }

    public LiveData<List<PulseRateMeasurementRecord>> get(LocalDate localDate) {
        return pulseRateMeasurementDao.get(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> getAverage(LocalDate localDate) {
        return pulseRateMeasurementDao.getAverage(localDate, localDate.plusDays(1));
    }

}
