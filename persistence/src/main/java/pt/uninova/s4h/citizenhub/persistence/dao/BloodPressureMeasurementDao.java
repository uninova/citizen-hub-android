package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailBloodPressureUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;

@Dao
public interface BloodPressureMeasurementDao {

    @Insert
    long insert(BloodPressureMeasurementRecord record);

    @Query("INSERT INTO blood_pressure_measurement (sample_id, systolic, diastolic, mean_arterial_pressure) VALUES (:sampleId, :systolic, :diastolic, :meanArterialPressure)")
    long insert(Long sampleId, Double systolic, Double diastolic, Double meanArterialPressure);

    @Query("SELECT blood_pressure_measurement.* FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<BloodPressureMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate) / 3600000) % 24 AS hour, "
            + " blood_pressure_measurement.systolic AS systolic, blood_pressure_measurement.diastolic AS diastolic, blood_pressure_measurement.mean_arterial_pressure AS mean "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) "
            + " SELECT systolic AS systolic, diastolic AS diastolic, mean AS mean, hour AS time FROM agg GROUP BY hour ")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailBloodPressureUtil> selectLastDay(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from) / 86400000) % :days AS day, "
            + " blood_pressure_measurement.systolic AS systolic, blood_pressure_measurement.diastolic AS diastolic, blood_pressure_measurement.mean_arterial_pressure AS mean "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT systolic AS systolic, diastolic AS diastolic, mean AS mean, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailBloodPressureUtil> selectSeveralDays(LocalDate from, LocalDate to, int days);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate) / 3600000) % 24 AS hour, blood_pressure_measurement.systolic AS value " +
            " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id " +
            " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) " +
            " SELECT value AS value, hour AS time FROM agg GROUP BY hour ")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastDaySystolic(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from) / 86400000) % 7 AS day, blood_pressure_measurement.systolic AS value "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT value AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastSevenDaysSystolic(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from) / 86400000) % 30 AS day, blood_pressure_measurement.systolic AS value "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT value AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastThirtyDaysSystolic(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate) / 3600000) % 24 AS hour, blood_pressure_measurement.diastolic AS value " +
            " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id " +
            " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) " +
            " SELECT value AS value, hour AS time FROM agg GROUP BY hour ")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastDayDiastolic(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from) / 86400000) % 7 AS day, blood_pressure_measurement.diastolic AS value "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT value AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastSevenDaysDiastolic(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from) / 86400000) % 30 AS day, blood_pressure_measurement.diastolic AS value "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT value AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastThirtyDaysDiastolic(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate) / 3600000) % 24 AS hour, blood_pressure_measurement.mean_arterial_pressure AS value " +
            " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id " +
            " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) " +
            " SELECT value AS value, hour AS time FROM agg GROUP BY hour ")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastDayMean(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from) / 86400000) % 7 AS day, blood_pressure_measurement.mean_arterial_pressure AS value "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT value AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastSevenDaysMean(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from) / 86400000) % 30 AS day, blood_pressure_measurement.mean_arterial_pressure AS value "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT value AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastThirtyDaysMean(LocalDate from, LocalDate to);

}