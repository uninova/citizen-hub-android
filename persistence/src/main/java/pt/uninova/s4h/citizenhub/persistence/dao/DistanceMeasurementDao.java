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

    @Query(value = "SELECT IFNULL(distance,0) + IFNULL(snapshotDistance, 0) FROM (SELECT SUM(distance_measurement.value) AS distance FROM distance_measurement INNER JOIN sample ON distance_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp DESC LIMIT 1) LEFT JOIN (SELECT distance_snapshot_measurement.value AS snapshotDistance FROM distance_snapshot_measurement INNER JOIN sample ON distance_snapshot_measurement.sample_id = sample_id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp DESC LIMIT 1)")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<Double> getDistanceAllTypes(LocalDate from, LocalDate to);

}