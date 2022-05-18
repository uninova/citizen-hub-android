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

@Dao
public interface HeartRateMeasurementDao {

    @Query(value = "SELECT heart_rate_measurement.* FROM heart_rate_measurement INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<HeartRateMeasurementRecord>> get(LocalDate from, LocalDate to);

    @Query("SELECT AVG(heart_rate_measurement.value) FROM heart_rate_measurement INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> getAverage(LocalDate from, LocalDate to);

    @Insert
    void insert(HeartRateMeasurementRecord record);

    @Query("INSERT INTO heart_rate_measurement (sample_id, value) VALUES (:sampleId, :value)")
    void insert(Long sampleId, Integer value);
}
