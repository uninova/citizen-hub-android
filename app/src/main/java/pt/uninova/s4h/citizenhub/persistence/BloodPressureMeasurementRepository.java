package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

public class BloodPressureMeasurementRepository {

    private final BloodPressureMeasurementDao bloodPressureMeasurementDao;

    public BloodPressureMeasurementRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        bloodPressureMeasurementDao = citizenHubDatabase.bloodPressureDao();
    }

    public void add(BloodPressureMeasurementRecord measurement) {
        CitizenHubDatabase.executorService().execute(() -> bloodPressureMeasurementDao.insert(measurement));
    }

    public LiveData<List<BloodPressureMeasurementRecord>> get(LocalDate localDate) {
        return bloodPressureMeasurementDao.get(localDate, localDate.plusDays(1));
    }

}
