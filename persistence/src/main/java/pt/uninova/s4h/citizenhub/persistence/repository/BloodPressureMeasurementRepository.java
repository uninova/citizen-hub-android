package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.BloodPressureMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class BloodPressureMeasurementRepository {

    private final BloodPressureMeasurementDao bloodPressureMeasurementDao;

    public BloodPressureMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        bloodPressureMeasurementDao = citizenHubDatabase.bloodPressureMeasurementDao();
    }

    public void create(BloodPressureMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> bloodPressureMeasurementDao.insert(record));
    }

    public LiveData<List<BloodPressureMeasurementRecord>> read(LocalDate localDate) {
        return bloodPressureMeasurementDao.selectLiveData(localDate, localDate.plusDays(1));
    }

    public void readLastDaySystolic(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastDaySystolic(localDate)));
    }

    public void readLastSevenDaysSystolic(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastSevenDaysSystolic(localDate.minusDays(6), localDate)));
    }

    public void readLastThirtyDaysSystolic(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastThirtyDaysSystolic(localDate.minusDays(29), localDate)));
    }

    public void readLastDayDiastolic(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastDayDiastolic(localDate)));
    }

    public void readLastSevenDaysDiastolic(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastSevenDaysDiastolic(localDate.minusDays(6), localDate)));
    }

    public void readLastThirtyDaysDiastolic(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastThirtyDaysDiastolic(localDate.minusDays(29), localDate)));
    }

    public void readLastDayMean(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastDayMean(localDate)));
    }

    public void readLastSevenDaysMean(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastSevenDaysMean(localDate.minusDays(6), localDate)));
    }

    public void readLastThirtyDaysMean(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(bloodPressureMeasurementDao.selectLastThirtyDaysMean(localDate.minusDays(29), localDate)));
    }

}