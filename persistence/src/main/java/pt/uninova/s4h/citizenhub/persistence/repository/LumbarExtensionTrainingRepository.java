package pt.uninova.s4h.citizenhub.persistence.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.data.LumbarExtensionTrainingMeasurement;
import pt.uninova.s4h.citizenhub.data.LumbarExtensionTrainingValue;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.CaloriesMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.dao.DeviceDao;
import pt.uninova.s4h.citizenhub.persistence.dao.LumbarExtensionTrainingDao;
import pt.uninova.s4h.citizenhub.persistence.dao.SampleDao;
import pt.uninova.s4h.citizenhub.persistence.entity.CaloriesMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.DeviceRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.LumbarExtensionTrainingMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.SampleRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.LumbarExtensionTrainingSummary;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class LumbarExtensionTrainingRepository {

    private final CaloriesMeasurementDao caloriesMeasurementDao;
    private final DeviceDao deviceDao;
    private final LumbarExtensionTrainingDao lumbarExtensionTrainingDao;
    private final SampleDao sampleDao;

    public LumbarExtensionTrainingRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        caloriesMeasurementDao = citizenHubDatabase.caloriesMeasurementDao();
        deviceDao = citizenHubDatabase.deviceDao();
        lumbarExtensionTrainingDao = citizenHubDatabase.lumbarExtensionTrainingDao();
        sampleDao = citizenHubDatabase.sampleDao();
    }

    public void create(LumbarExtensionTrainingMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.insert(record));
    }

    public void delete() {
        CitizenHubDatabase.executorService().execute(lumbarExtensionTrainingDao::delete);
    }

    public void delete(LumbarExtensionTrainingMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.delete(record));
    }

    public void read(Long sampleId, Observer<Sample> observer) {
        CitizenHubDatabase.executorService().execute(() -> {
            final SampleRecord sampleRecord = sampleDao.select(sampleId);
            final LumbarExtensionTrainingMeasurementRecord lumbarExtensionTrainingMeasurementRecord = lumbarExtensionTrainingDao.select(sampleId);
            final CaloriesMeasurementRecord caloriesMeasurementRecord = caloriesMeasurementDao.select(sampleId);

            final Instant timestamp = sampleRecord.getTimestamp();

            final boolean hasLumbarExtensionTrainingMeasurement = lumbarExtensionTrainingMeasurementRecord != null;
            final boolean hasCaloriesMeasurement = caloriesMeasurementRecord != null;

            final Measurement<?>[] measurements = new Measurement<?>[(hasLumbarExtensionTrainingMeasurement ? 1 : 0) + (hasCaloriesMeasurement ? 1 : 0)];
            int index = 0;

            if (hasLumbarExtensionTrainingMeasurement) {
                measurements[index++] = lumbarExtensionTrainingMeasurementRecord.toLumbarExtensionTrainingMeasurement();
            }

            if (hasCaloriesMeasurement) {
                measurements[index] = caloriesMeasurementRecord.toCaloriesMeasurement();
            }

            final Sample sample = new Sample(timestamp, null, measurements);

            observer.observe(sample);
        });

    }

    public LiveData<List<LumbarExtensionTrainingMeasurementRecord>> read(LocalDate localDate) {
        return lumbarExtensionTrainingDao.selectLiveData(localDate, localDate.plusDays(1));
    }

    public void read(LocalDate localDate, Observer<List<LumbarExtensionTrainingMeasurementRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.select(localDate, localDate.plusDays(1))));
    }

    public LiveData<LumbarExtensionTrainingSummary> readLatest(LocalDate localDate) {
        return lumbarExtensionTrainingDao.selectLatestLiveData(localDate, localDate.plusDays(1));
    }

    public void update(LumbarExtensionTrainingMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> lumbarExtensionTrainingDao.update(record));
    }

    public void readLastDayDuration(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastDayDuration(localDate)));
    }

    public void readLastSevenDaysDuration(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastSevenDaysDuration(localDate.minusDays(7), localDate)));
    }

    public void readLastThirtyDaysDuration(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastThirtyDaysDuration(localDate.minusDays(30), localDate)));
    }

    public void readLastDayScore(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastDayScore(localDate)));
    }

    public void readLastSevenDaysScore(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastSevenDaysScore(localDate.minusDays(7), localDate)));
    }

    public void readLastThirtyDaysScore(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastThirtyDaysScore(localDate.minusDays(30), localDate)));
    }

    public void readLastDayRepetitions(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastDayRepetitions(localDate)));
    }

    public void readLastSevenDaysRepetitions(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastSevenDaysRepetitions(localDate.minusDays(7), localDate)));
    }

    public void readLastThirtyDaysRepetitions(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastThirtyDaysRepetitions(localDate.minusDays(30), localDate)));
    }

    public void readLastDayWeight(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastDayWeight(localDate)));
    }

    public void readLastSevenDaysWeight(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastSevenDaysWeight(localDate.minusDays(7), localDate)));
    }

    public void readLastThirtyDaysWeight(LocalDate localDate, Observer<List<SummaryDetailUtil>> observer){
        CitizenHubDatabase.executorService().execute(() -> observer.observe(lumbarExtensionTrainingDao.selectLastThirtyDaysWeight(localDate.minusDays(30), localDate)));
    }
}