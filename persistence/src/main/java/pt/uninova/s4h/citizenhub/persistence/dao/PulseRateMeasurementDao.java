package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.PulseRateMeasurementRecord;

@Dao
public interface PulseRateMeasurementDao {

    @Query(value = "SELECT pulse_rate_measurement.* FROM pulse_rate_measurement INNER JOIN sample ON pulse_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<PulseRateMeasurementRecord>> get(LocalDate from, LocalDate to);

    @Query("SELECT AVG(value) FROM pulse_rate_measurement INNER JOIN sample ON pulse_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> getAverage(LocalDate from, LocalDate to);

    @Insert
    void insert(PulseRateMeasurementRecord record);

    @Query("INSERT INTO pulse_rate_measurement (sample_id, value) VALUES (:sampleId, :value)")
    void insert(Long sampleId, Double value);
}
