package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import pt.uninova.s4h.citizenhub.data.BloodPressureMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;

public class SampleRepository {
    private final SampleDao sampleDao;
    private final BloodPressureDao bloodPressureDao;
    public SampleRepository(Application application) {

        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        sampleDao = citizenHubDatabase.sampleDao();
        bloodPressureDao = citizenHubDatabase.bloodPressureDao();
    }

    public void addSample(Sample sample){
        CitizenHubDatabase.executorService().execute(() -> {
            sampleDao.insert(sample);
        });
    }
}
