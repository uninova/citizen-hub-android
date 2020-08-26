package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import java.util.List;

public class MeasurementRepository {

    private final MeasurementDao measurementDao;

    public MeasurementRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        measurementDao = citizenHubDatabase.measurementDao();
    }

    public void add(Measurement measurement) {
        CitizenHubDatabase.executorService().execute(() -> {
            measurementDao.addMeasurement(measurement);
        });
    }

    public List<Measurement> getAllMeasurements() {
        return measurementDao.getMeasurements();
    }

}
