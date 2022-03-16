package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.room.Transaction;

import java.util.List;

public class SampleRepository {
    private final SampleDao sampleDao;

    public SampleRepository(Application application) {

        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        sampleDao = citizenHubDatabase.sampleDao();
    }

    public void insertSample(List<SampleWithMeasurements> sampleWithMeasurements, CitizenHubDatabase database){
        if (sampleWithMeasurements != null && database != null) {
            sampleDao.insertSampleWithMeasurements(sampleWithMeasurements);
            for (SampleWithMeasurements sample : sampleWithMeasurements) {
                database.bloodPressureDao().insert(sample.getBloodPressureMeasurement());
            }
        }
    }
}
