package pt.uninova.s4h.citizenhub.persistence.dao;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.StepsMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.WalkingInformation;

@Dao
public interface StepsMeasurementDao {

    @Insert
    long insert(StepsMeasurementRecord record);

    @Query("INSERT INTO steps_measurement (sample_id, value) VALUES (:sampleId, :value)")
    long insert(Long sampleId, Double value);

    @Query("SELECT * FROM steps_measurement WHERE sample_id = :sampleId")
    StepsMeasurementRecord select(Long sampleId);

    @Query(value = "SELECT steps_measurement.* FROM steps_measurement INNER JOIN sample ON steps_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<StepsMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

    @Query(value = "SELECT MAX(value) FROM steps_measurement INNER JOIN sample ON steps_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> selectMaximumLiveData(LocalDate from, LocalDate to);

    @Query(value = "SELECT steps_measurement.value AS steps, distance_measurement.value AS distance, calories_measurement.value AS calories FROM steps_measurement INNER JOIN sample ON steps_measurement.sample_id = sample.id LEFT JOIN distance_measurement ON sample.id = distance_measurement.sample_id LEFT JOIN calories_measurement ON sample.id = calories_measurement.sample_id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp DESC LIMIT 1")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<WalkingInformation> selectLatestWalkingInformationLiveData(LocalDate from, LocalDate to);

    @Query(value = "WITH sample_window(id) AS (SELECT id FROM sample WHERE timestamp >= :from AND timestamp < :to), discreet (value) AS (SELECT IFNULL(SUM(steps_measurement.value), 0) AS value FROM sample_window INNER JOIN steps_measurement ON sample_window.id = steps_measurement.sample_id), snapshot (value) AS (SELECT IFNULL(MAX(steps_snapshot_measurement.value), 0) AS value FROM sample_window INNER JOIN steps_snapshot_measurement ON sample_window.id = steps_snapshot_measurement.sample_id) SELECT discreet.value + snapshot.value AS value FROM discreet, snapshot;")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Integer> getStepsAllTypes(LocalDate from, LocalDate to);
}