package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;

import java.time.Instant;
import java.time.LocalDate;

import pt.uninova.s4h.citizenhub.data.BloodPressureMeasurement;
import pt.uninova.s4h.citizenhub.data.BloodPressureValue;
import pt.uninova.s4h.citizenhub.data.CaloriesMeasurement;
import pt.uninova.s4h.citizenhub.data.DistanceMeasurement;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.PostureMeasurement;
import pt.uninova.s4h.citizenhub.data.PostureValue;
import pt.uninova.s4h.citizenhub.data.PulseRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.StepsMeasurement;
import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;

@Dao
public abstract class SampleDao {

    private final BloodPressureMeasurementDao bloodPressureMeasurementDao;
    private final DailyCaloriesMeasurementDao dailyCaloriesMeasurementDao;
    private final DailyDistanceMeasurementDao dailyDistanceMeasurementDao;
    private final DailyStepsMeasurementDao dailyStepsMeasurementDao;
    private final HeartRateMeasurementDao heartRateMeasurementDao;
    private final PostureMeasurementDao postureMeasurementDao;
    private final PulseRateMeasurementDao pulseRateMeasurementDao;

    public SampleDao(CitizenHubDatabase database) {
        bloodPressureMeasurementDao = database.bloodPressureMeasurementDao();
        dailyCaloriesMeasurementDao = database.dailyCaloriesMeasurementDao();
        dailyDistanceMeasurementDao = database.dailyDistanceMeasurementDao();
        dailyStepsMeasurementDao = database.dailyStepsMeasurementDao();
        heartRateMeasurementDao = database.heartRateMeasurementDao();
        postureMeasurementDao = database.postureMeasurementDao();
        pulseRateMeasurementDao = database.pulseRateMeasurementDao();
    }

    @Query("SELECT COUNT(*) > 0 FROM sample WHERE timestamp >= :from AND timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    public abstract LiveData<Boolean> hasRows(LocalDate from, LocalDate to);

    @Insert
    public abstract long insert(SampleRecord sampleRecord);

    @Query("INSERT INTO sample (device_id, timestamp) VALUES ((SELECT id FROM device WHERE device.address = :address), :timestamp)")
    @TypeConverters(EpochTypeConverter.class)
    public abstract long insert(String address, Instant timestamp);

    @Transaction
    public void insert(Sample sample) {
        final int sampleId = (int) insert(sample.getSource().getAddress(), sample.getTimestamp());

        for (Measurement<?> measurement : sample.getMeasurements()) {
            switch (measurement.getType()) {
                case Measurement.TYPE_BLOOD_PRESSURE:
                    final BloodPressureValue bloodPressureValue = ((BloodPressureMeasurement) measurement).getValue();

                    bloodPressureMeasurementDao.insert(sampleId, bloodPressureValue.getSystolic(), bloodPressureValue.getDiastolic(), bloodPressureValue.getMeanArterialPressure());
                    break;
                case Measurement.TYPE_DAILY_CALORIES:
                    final Double caloriesValue = ((CaloriesMeasurement) measurement).getValue();

                    dailyCaloriesMeasurementDao.insert(sampleId, caloriesValue);
                    break;
                case Measurement.TYPE_DAILY_DISTANCE:
                    final Double distanceValue = ((DistanceMeasurement) measurement).getValue();

                    dailyDistanceMeasurementDao.insert(sampleId, distanceValue);
                    break;
                case Measurement.TYPE_HEART_RATE:
                    final Integer heartRateValue = ((HeartRateMeasurement) measurement).getValue();

                    heartRateMeasurementDao.insert(sampleId, heartRateValue);
                    break;
                case Measurement.TYPE_POSTURE:
                    final PostureValue postureValue = ((PostureMeasurement) measurement).getValue();

                    postureMeasurementDao.insert(sampleId, postureValue.getClassification(), postureValue.getDuration());
                    break;
                case Measurement.TYPE_PULSE_RATE:
                    final Double pulseRateValue = ((PulseRateMeasurement) measurement).getValue();

                    pulseRateMeasurementDao.insert(sampleId, pulseRateValue);
                    break;
                case Measurement.TYPE_DAILY_STEPS:
                    final Integer stepsValue = ((StepsMeasurement) measurement).getValue();

                    dailyStepsMeasurementDao.insert(sampleId, stepsValue);
                    break;
            }
        }
    }
}

