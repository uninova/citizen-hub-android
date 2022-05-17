package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;

import pt.uninova.s4h.citizenhub.data.Sample;

public class SampleRepository {
    private final SampleDao sampleDao;
    private final BloodPressureMeasurementDao bloodPressureMeasurementDao;

    public SampleRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        sampleDao = citizenHubDatabase.sampleDao();
        bloodPressureMeasurementDao = citizenHubDatabase.bloodPressureMeasurementDao();
    }

    public void create(Sample sample) {
        CitizenHubDatabase.executorService().execute(() -> {
            sampleDao.insert(sample);
        });
    }

    public LiveData<Boolean> hasRecords(LocalDate date) {
        return sampleDao.hasRows(date, date.plusDays(1));
    }
}
