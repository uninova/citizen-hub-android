package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.PostureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.PostureClassificationSum;

@Dao
public interface PostureMeasurementDao {

    @Query(value = "SELECT posture_measurement.* FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<PostureMeasurementRecord>> get(LocalDate from, LocalDate to);

    @Query("SELECT posture_measurement.classification AS classification, SUM(posture_measurement.duration) AS duration FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to GROUP BY classification")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<PostureClassificationSum>> getSum(LocalDate from, LocalDate to);

    @Insert
    long insert(PostureMeasurementRecord record);

    @Query("INSERT INTO posture_measurement (sample_id, classification, duration) VALUES (:sampleId, :classification, :duration)")
    void insert(Long sampleId, Integer classification, Double duration);

}
