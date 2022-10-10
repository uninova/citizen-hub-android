package pt.uninova.s4h.citizenhub.persistence.dao;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.DistanceMeasurementRecord;

@Dao
public interface DistanceMeasurementDao {

    @Insert
    long insert(DistanceMeasurementRecord record);

    @Query("INSERT INTO distance_measurement (sample_id, value) VALUES (:sampleId, :value)")
    long insert(Long sampleId, Double value);

    @Query("SELECT * FROM distance_measurement WHERE sample_id = :sampleId")
    DistanceMeasurementRecord select(Long sampleId);

    @Query(value = "SELECT distance_measurement.* FROM distance_measurement INNER JOIN sample ON distance_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<DistanceMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

    @Query(value = "SELECT MAX(value) FROM distance_measurement INNER JOIN sample ON distance_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> selectMaximumLiveData(LocalDate from, LocalDate to);

    @Query(value = "WITH sample_window(id) AS (SELECT id FROM sample WHERE timestamp >= :from AND timestamp < :to), discreet (value) AS (SELECT SUM(distance_measurement.value) AS value FROM sample_window INNER JOIN distance_measurement ON sample_window.id = distance_measurement.sample_id), snapshot (value) AS (SELECT MAX(distance_snapshot_measurement.value) AS value FROM sample_window INNER JOIN distance_snapshot_measurement ON sample_window.id = distance_snapshot_measurement.sample_id) SELECT SUM(value) AS value FROM (SELECT value FROM discreet UNION ALL SELECT value FROM snapshot);")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> getDistanceAllTypes(LocalDate from, LocalDate to);

}