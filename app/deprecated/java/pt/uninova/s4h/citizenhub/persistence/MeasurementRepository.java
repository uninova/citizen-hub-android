package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MeasurementRepository {

    private final MeasurementDao measurementDao;

    public MeasurementRepository(Application application) {
        CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        measurementDao = citizenHubDatabase.measurementDao();
    }

    public void add(Measurement measurement) {
        CitizenHubDatabase.executorService().execute(() -> {
            measurementDao.addMeasurement(measurement);
        });
    }

    public LiveData<List<Measurement>> getAllMeasurementsLive() {
        return measurementDao.getMeasurements();
    }

}
