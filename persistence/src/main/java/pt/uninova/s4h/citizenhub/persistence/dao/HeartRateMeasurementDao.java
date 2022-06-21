package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.HeartRateMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.AggregateSummary;

@Dao
public interface HeartRateMeasurementDao {

    @Insert
    long insert(HeartRateMeasurementRecord record);

    @Query("INSERT INTO heart_rate_measurement (sample_id, value) VALUES (:sampleId, :value)")
    long insert(Long sampleId, Integer value);

    @Query(value = "SELECT heart_rate_measurement.* FROM heart_rate_measurement INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<HeartRateMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

    @Query("SELECT AVG(heart_rate_measurement.value) AS average, MAX(heart_rate_measurement.value) AS maximum, MIN(heart_rate_measurement.value) AS minimum FROM heart_rate_measurement INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<AggregateSummary> selectAggregateLiveData(LocalDate from, LocalDate to);

    @Query("SELECT AVG(heart_rate_measurement.value) FROM heart_rate_measurement INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> selectAverageLiveData(LocalDate from, LocalDate to);

}