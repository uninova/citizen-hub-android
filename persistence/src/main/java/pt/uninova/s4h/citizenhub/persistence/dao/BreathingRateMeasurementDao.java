package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.BreathingRateMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.AggregateSummary;

@Dao
public interface BreathingRateMeasurementDao {

    @Insert
    long insert(BreathingRateMeasurementRecord record);

    @Query("INSERT INTO breathing_rate_measurement (sample_id, value) VALUES (:sampleId, :value)")
    long insert(Long sampleId, Integer value);

    @Query("SELECT breathing_rate_measurement.* FROM breathing_rate_measurement INNER JOIN sample ON breathing_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<BreathingRateMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

    @Query("SELECT AVG(breathing_rate_measurement.value) AS average, MAX(breathing_rate_measurement.value) AS maximum, MIN(breathing_rate_measurement.value) AS minimum FROM breathing_rate_measurement INNER JOIN sample ON breathing_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<AggregateSummary> selectAggregateLiveData(LocalDate from, LocalDate to);

    @Query("SELECT AVG(breathing_rate_measurement.value) FROM breathing_rate_measurement INNER JOIN sample ON breathing_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> selectAverageLiveData(LocalDate from, LocalDate to);
}