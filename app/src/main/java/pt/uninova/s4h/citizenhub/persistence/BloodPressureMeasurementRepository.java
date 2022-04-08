package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

public class BloodPressureMeasurementRepository {

        private final BloodPressureDao bloodPressureDao;

        public BloodPressureMeasurementRepository(Application application) {
            final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

            bloodPressureDao = citizenHubDatabase.bloodPressureDao();
        }

        public void add(BloodPressureRecord measurement) {
            CitizenHubDatabase.executorService().execute(() -> {
                bloodPressureDao.insert(measurement);
            });
        }
}
