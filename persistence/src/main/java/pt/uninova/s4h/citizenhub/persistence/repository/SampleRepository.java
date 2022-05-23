package pt.uninova.s4h.citizenhub.persistence.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;

import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.BloodPressureMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.SampleDao;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

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

    public void create(Sample sample, Observer<Long> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            observer.observe(sampleDao.insert(sample));
        });
    }

    public LiveData<Boolean> hasRecords(LocalDate date) {
        return sampleDao.hasRows(date, date.plusDays(1));
    }
}
