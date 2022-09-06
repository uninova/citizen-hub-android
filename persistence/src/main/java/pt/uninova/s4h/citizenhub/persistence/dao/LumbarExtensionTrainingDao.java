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
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;

@Dao
public interface LumbarExtensionTrainingDao {

    @Delete
    void delete(LumbarExtensionTrainingMeasurementRecord record);

    @Query("DELETE FROM lumbar_extension_training_measurement")
    void delete();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(LumbarExtensionTrainingMeasurementRecord record);

    @Query("INSERT INTO lumbar_extension_training_measurement (sample_id, duration, score, repetitions, weight) VALUES (:sampleId, :duration, :score, :repetitions, :weight)")
    @TypeConverters(DurationTypeConverter.class)
    long insert(Long sampleId, Duration duration, Double score, Integer repetitions, Integer weight);

    @Query("SELECT * FROM lumbar_extension_training_measurement WHERE sample_id = :sampleId")
    LumbarExtensionTrainingMeasurementRecord select(Long sampleId);

    @Query("SELECT lumbar_extension_training_measurement.* FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<LumbarExtensionTrainingMeasurementRecord> select(LocalDate from, LocalDate to);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LumbarExtensionTrainingMeasurementRecord record);

    @Query(value = "SELECT lumbar_extension_training_measurement.* FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<LumbarExtensionTrainingMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

    @Query(value = "SELECT duration, repetitions, weight, calories_measurement.value AS calories FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id LEFT JOIN calories_measurement ON sample.id = calories_measurement.sample_id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp DESC LIMIT 1")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<LumbarExtensionTrainingSummary> selectLatestLiveData(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate - 3600000) / 3600000) % 24 AS hour, lumbar_extension_training_measurement.duration AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) "
            + " SELECT value  AS value, hour AS time FROM agg GROUP BY hour")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastDayDuration(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 7 AS day, lumbar_extension_training_measurement.duration AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT MAX(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastSevenDaysDuration(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 30 AS day, lumbar_extension_training_measurement.duration AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT AVG(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastThirtyDaysDuration(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate - 3600000) / 3600000) % 24 AS hour, lumbar_extension_training_measurement.score AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) "
            + " SELECT value  AS value, hour AS time FROM agg GROUP BY hour")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastDayScore(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 7 AS day, lumbar_extension_training_measurement.score AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT MAX(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastSevenDaysScore(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 30 AS day, lumbar_extension_training_measurement.score AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT AVG(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastThirtyDaysScore(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate - 3600000) / 3600000) % 24 AS hour, lumbar_extension_training_measurement.repetitions AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) "
            + " SELECT value  AS value, hour AS time FROM agg GROUP BY hour")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastDayRepetitions(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 7 AS day, lumbar_extension_training_measurement.repetitions AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT MAX(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastSevenDaysRepetitions(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 30 AS day, lumbar_extension_training_measurement.repetitions AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT AVG(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastThirtyDaysRepetitions(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :localDate - 3600000) / 3600000) % 24 AS hour, lumbar_extension_training_measurement.weight AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :localDate AND sample.timestamp < :localDate + 86400000) "
            + " SELECT value  AS value, hour AS time FROM agg GROUP BY hour")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastDayWeight(LocalDate localDate);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 7 AS day, lumbar_extension_training_measurement.weight AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT MAX(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastSevenDaysWeight(LocalDate from, LocalDate to);

    @Query(value = "WITH agg AS(SELECT ((sample.timestamp - :from - 86400000) / 86400000) % 30 AS day, lumbar_extension_training_measurement.weight AS value "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to + 86400000) "
            + " SELECT AVG(value) AS value, day AS time FROM agg GROUP BY day")
    @TypeConverters(EpochTypeConverter.class)
    List<SummaryDetailUtil> selectLastThirtyDaysWeight(LocalDate from, LocalDate to);

}