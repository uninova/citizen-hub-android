package pt.uninova.s4h.citizenhub.persistence.dao;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.CaloriesMeasurementRecord;

@Dao
public interface CaloriesMeasurementDao {

    @Insert
    long insert(CaloriesMeasurementRecord record);

    @Query("INSERT INTO calories_measurement (sample_id, value) VALUES (:sampleId, :value)")
    long insert(Long sampleId, Double value);

    @Query("SELECT * FROM calories_measurement WHERE sample_id = :sampleId")
    CaloriesMeasurementRecord select(Long sampleId);

    @Query(value = "SELECT calories_measurement.* FROM calories_measurement INNER JOIN sample ON calories_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<CaloriesMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

    @Query(value = "SELECT MAX(value) FROM calories_measurement INNER JOIN sample ON calories_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> selectMaximumLiveData(LocalDate from, LocalDate to);

    @Query(value = "WITH sample_window(id) AS (SELECT id FROM sample WHERE timestamp >= :from AND timestamp < :to), discreet (value) AS (SELECT IFNULL(SUM(calories_measurement.value), 0) AS value FROM sample_window INNER JOIN calories_measurement ON sample_window.id = calories_measurement.sample_id), snapshot (value) AS (SELECT IFNULL(MAX(calories_snapshot_measurement.value), 0) AS value FROM sample_window INNER JOIN calories_snapshot_measurement ON sample_window.id = calories_snapshot_measurement.sample_id) SELECT discreet.value + snapshot.value AS value FROM discreet, snapshot;")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> getCaloriesAllTypes(LocalDate from, LocalDate to);
}