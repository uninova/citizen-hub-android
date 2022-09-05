package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.HeartRateMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.HeartRateMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.AggregateSummary;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class HeartRateMeasurementRepository {

    private final HeartRateMeasurementDao heartRateMeasurementDao;

    public HeartRateMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        heartRateMeasurementDao = citizenHubDatabase.heartRateMeasurementDao();
    }

    public void create(HeartRateMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> heartRateMeasurementDao.insert(record));
    }

    public LiveData<List<HeartRateMeasurementRecord>> read(LocalDate localDate) {
        return heartRateMeasurementDao.selectLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<AggregateSummary> readAggregate(LocalDate localDate) {
        return heartRateMeasurementDao.selectAggregateLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<Double> readAverage(LocalDate localDate) {
        return heartRateMeasurementDao.selectAverageLiveData(localDate, localDate.plusDays(1));
    }

    public void readAverageObserved(LocalDate localDate, Observer<Double> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(heartRateMeasurementDao.selectAverage(localDate, localDate.plusDays(1))));
    }
    
    public void readLastDay(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(heartRateMeasurementDao.selectLastDay(localDate)));
    }

    public void readLastSevenDays(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(heartRateMeasurementDao.selectLastSevenDays(localDate.minusDays(7), localDate)));
    }

    public void readAverageLastThirtyDays(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(heartRateMeasurementDao.selectAverageLastThirtyDays(localDate.minusDays(30), localDate)));
    }

    public void readMaxLastThirtyDays(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(heartRateMeasurementDao.selectMaxLastThirtyDays(localDate.minusDays(30), localDate)));
    }

    public void readMinLastThirtyDays(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(heartRateMeasurementDao.selectMinLastThirtyDays(localDate.minusDays(30), localDate)));
    }

}