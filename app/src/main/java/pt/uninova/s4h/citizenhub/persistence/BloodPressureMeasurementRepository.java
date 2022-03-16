package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import pt.uninova.util.WorkTimeRangeConverter;

public class BloodPressureMeasurementRepository {

        private final BloodPressureDao bloodPressureDao;

        public BloodPressureMeasurementRepository(Application application) {
            final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

            bloodPressureDao = citizenHubDatabase.bloodPressureDao();
        }

        public void add(BloodPressureMeasurement measurement) {
            CitizenHubDatabase.executorService().execute(() -> {
                bloodPressureDao.insert(measurement);
            });
        }
}
