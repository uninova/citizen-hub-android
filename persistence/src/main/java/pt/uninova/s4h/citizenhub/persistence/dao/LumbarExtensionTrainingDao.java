package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.DurationTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.LumbarExtensionTrainingMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.LumbarExtensionTrainingSummary;

@Dao
public interface LumbarExtensionTrainingDao {

    @Delete
    void delete(LumbarExtensionTrainingMeasurementRecord record);

    @Query("DELETE FROM lumbar_extension_training_measurement")
    void deleteAll();

    @Query(value = "SELECT lumbar_extension_training_measurement.* FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<LumbarExtensionTrainingMeasurementRecord>> get(LocalDate from, LocalDate to);

    @Query(value = "SELECT duration, repetitions, weight, calories_measurement.value AS calories FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id LEFT JOIN calories_measurement ON sample.id = calories_measurement.sample_id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp DESC LIMIT 1")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<LumbarExtensionTrainingSummary> getLatest(LocalDate from, LocalDate to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LumbarExtensionTrainingMeasurementRecord record);

    @Query("INSERT INTO lumbar_extension_training_measurement (sample_id, duration, score, repetitions, weight) VALUES (:sampleId, :duration, :score, :repetitions, :weight)")
    @TypeConverters(DurationTypeConverter.class)
    void insert(long sampleId, Duration duration, Double score, Integer repetitions, Integer weight);

    @Query("SELECT lumbar_extension_training_measurement.* FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<LumbarExtensionTrainingMeasurementRecord> select(LocalDate from, LocalDate to);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LumbarExtensionTrainingMeasurementRecord record);
}
