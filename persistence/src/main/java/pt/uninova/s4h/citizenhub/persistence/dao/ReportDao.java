package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;


import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.util.ReportUtil;

@Dao
public interface ReportDao {

    @Query("SELECT dailyCalories.calories, dailyDistance.distance, "
            + " heartRate.maxHeartRate, heartRate.minHeartRate, heartRate.avgHeartRate, "
            + " correctPosture.correctPostureDuration, wrongPosture.wrongPostureDuration, "
            + " dailySteps.steps"
            + " FROM "
            + " (SELECT MAX(value) AS calories "
            + " FROM calories_snapshot_measurement INNER JOIN sample ON calories_snapshot_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to) dailyCalories, "
            + " (SELECT MAX(value) AS distance "
            + " FROM distance_snapshot_measurement INNER JOIN sample ON distance_snapshot_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to) dailyDistance, "
            + " (SELECT MAX(value) AS maxHeartRate, MIN(value) AS minHeartRate, AVG(value) AS avgHeartRate "
            + " FROM heart_rate_measurement INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to) heartRate, "
            /*+ " (SELECT classification AS classification, SUM(duration) AS dailyPostureDuration "
            + " FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to GROUP BY classification) posture, "*/
            + " (SELECT classification AS classification, SUM(duration) AS correctPostureDuration "
            + " FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND classification = 1) correctPosture, "
            + "(SELECT classification AS classification, SUM(duration) AS wrongPostureDuration "
            + " FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND classification = 2) wrongPosture, "
            + "(SELECT MAX(value) AS steps "
            + " FROM steps_snapshot_measurement INNER JOIN sample ON steps_snapshot_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to) dailySteps")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getSimpleDailyRecords(LocalDate from, LocalDate to);

    @Query("SELECT dailyCalories.calories, dailyDistance.distance, "
            + " heartRate.maxHeartRate, heartRate.minHeartRate, heartRate.avgHeartRate, "
            + " correctPosture.correctPostureDuration, wrongPosture.wrongPostureDuration, "
            + " dailySteps.steps "
            + " FROM "

            + " (SELECT MAX(value) AS calories FROM calories_snapshot_measurement "
            + " INNER JOIN sample ON calories_snapshot_measurement.sample_id = sample.id "
            + " INNER JOIN tag ON calories_snapshot_measurement.sample_id = tag.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1) dailyCalories, "

            + " (SELECT MAX(value) AS distance FROM distance_snapshot_measurement "
            + " INNER JOIN sample ON distance_snapshot_measurement.sample_id = sample.id "
            + " INNER JOIN tag ON distance_snapshot_measurement.sample_id = tag.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1) dailyDistance, "

            + " (SELECT MAX(value) AS maxHeartRate, MIN(value) AS minHeartRate, AVG(value) AS avgHeartRate FROM heart_rate_measurement "
            + " INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id "
            + " INNER JOIN tag ON heart_rate_measurement.sample_id = tag.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1) heartRate, "
            /*+ " (SELECT classification AS classification, SUM(duration) AS dailyPostureDuration "
            + " FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to GROUP BY classification) posture, "*/
            + " (SELECT classification AS classification, SUM(duration) AS correctPostureDuration FROM posture_measurement "
            + " INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " INNER JOIN tag ON posture_measurement.sample_id = tag.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1 AND classification = 1) correctPosture, "

            + "(SELECT classification AS classification, SUM(duration) AS wrongPostureDuration FROM posture_measurement "
            + " INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " INNER JOIN tag ON posture_measurement.sample_id = tag.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1 AND classification = 2) wrongPosture, "

            + "(SELECT MAX(value) AS steps FROM steps_snapshot_measurement "
            + " INNER JOIN sample ON steps_snapshot_measurement.sample_id = sample.id "
            + " INNER JOIN tag ON steps_snapshot_measurement.sample_id = tag.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1) dailySteps")
    @TypeConverters(EpochTypeConverter.class)
    ReportUtil getWorkTimeSimpleRecords(LocalDate from, LocalDate to);

    @Query("SELECT dailyCalories.calories, dailyDistance.distance, "
            + " heartRate.maxHeartRate, heartRate.minHeartRate, heartRate.avgHeartRate, "
            + " correctPosture.correctPostureDuration, wrongPosture.wrongPostureDuration, "
            + " dailySteps.steps"
            + " FROM "

            + " (SELECT MAX(value) AS calories FROM calories_snapshot_measurement "
            + " INNER JOIN sample ON calories_snapshot_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to "
            + " AND NOT EXISTS "
            + " (SELECT tag.label FROM tag WHERE tag.sample_id = calories_snapshot_measurement.sample_id AND tag.label = 1)) dailyCalories, "

            + " (SELECT MAX(value) AS distance FROM distance_snapshot_measurement "
            + " INNER JOIN sample ON distance_snapshot_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to "
            + " AND NOT EXISTS "
            + " (SELECT tag.label FROM tag WHERE tag.sample_id = distance_snapshot_measurement.sample_id AND tag.label = 1)) dailyDistance, "

            + " (SELECT MAX(value) AS maxHeartRate, MIN(value) AS minHeartRate, AVG(value) AS avgHeartRate FROM heart_rate_measurement "
            + " INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to "
            + " AND NOT EXISTS "
            + " (SELECT tag.label FROM tag WHERE tag.sample_id = heart_rate_measurement.sample_id AND tag.label = 1)) heartRate, "
            /*+ " (SELECT classification AS classification, SUM(duration) AS dailyPostureDuration "
            + " FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to GROUP BY classification) posture, "*/
            + " (SELECT classification AS classification, SUM(duration) AS correctPostureDuration FROM posture_measurement "
            + " INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND classification = 1 "
            + " AND NOT EXISTS "
            + " (SELECT tag.label FROM tag WHERE tag.sample_id = posture_measurement.sample_id AND tag.label = 1)) correctPosture, "

            + " (SELECT classification AS classification, SUM(duration) AS wrongPostureDuration FROM posture_measurement "
            + " INNER JOIN sample ON posture_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND classification = 2 "
            + " AND NOT EXISTS "
            + "(SELECT tag.label FROM tag WHERE tag.sample_id = posture_measurement.sample_id AND tag.label = 1)) wrongPosture, "

            + "(SELECT MAX(value) AS steps FROM steps_snapshot_measurement "
            + " INNER JOIN sample ON steps_snapshot_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to "
            + " AND NOT EXISTS "
            + "(SELECT tag.label FROM tag WHERE tag.sample_id = steps_snapshot_measurement.sample_id AND tag.label = 1)) dailySteps")
    @TypeConverters(EpochTypeConverter.class)
    ReportUtil getNotWorkTimeSimpleRecords(LocalDate from, LocalDate to);

    @Query("SELECT diastolic, systolic, mean_arterial_pressure AS meanArterialPressure, sample.timestamp AS timestamp "
            + " FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getBloodPressure(LocalDate from, LocalDate to);

    @Query("SELECT diastolic, systolic, mean_arterial_pressure AS meanArterialPressure, sample.timestamp AS timestamp, "
            + " pulse_rate_measurement.value AS pulseRate FROM blood_pressure_measurement "
            + " INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " LEFT JOIN pulse_rate_measurement ON blood_pressure_measurement.sample_id = pulse_rate_measurement.sample_id "
            + " INNER JOIN tag ON blood_pressure_measurement.sample_id = tag.sample_id "
            + "WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1 ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getWorkTimeBloodPressure(LocalDate from, LocalDate to);

    @Query("SELECT diastolic, systolic, mean_arterial_pressure AS meanArterialPressure, sample.timestamp AS timestamp, "
            + " pulse_rate_measurement.value AS pulseRate FROM blood_pressure_measurement "
            + " INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id "
            + " LEFT JOIN pulse_rate_measurement ON blood_pressure_measurement.sample_id = pulse_rate_measurement.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to "
            + " AND NOT EXISTS "
            + "(SELECT tag.label FROM tag WHERE tag.sample_id = blood_pressure_measurement.sample_id AND tag.label = 1) ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getNotWorkTimeBloodPressure(LocalDate from, LocalDate to);

    @Query("SELECT MAX(value) AS calories FROM calories_snapshot_measurement INNER JOIN sample ON calories_snapshot_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getCalories(LocalDate from, LocalDate to);

    @Query("SELECT MAX(value) AS distance FROM distance_snapshot_measurement INNER JOIN sample ON distance_snapshot_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getDistance(LocalDate from, LocalDate to);

    @Query("SELECT AVG(heart_rate_measurement.value) AS avgHeartRate, MAX(heart_rate_measurement.value) AS maxHeartRate, MIN(heart_rate_measurement.value) AS minHeartRate FROM heart_rate_measurement INNER JOIN sample ON heart_rate_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getHeartRate(LocalDate from, LocalDate to);

    @Query("SELECT duration AS lumbarExtensionDuration, score AS lumbarExtensionScore, repetitions AS lumbarExtensionRepetitions, "
            + " weight AS lumbarExtensionWeight, sample.timestamp AS timestamp, calories_measurement.value AS calories "
            + " FROM lumbar_extension_training_measurement INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " LEFT JOIN calories_measurement ON sample.id = calories_measurement.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getLumbarExtensionTraining(LocalDate from, LocalDate to);

    @Query("SELECT duration AS lumbarExtensionDuration, score AS lumbarExtensionScore, repetitions AS lumbarExtensionRepetitions, "
            + " weight AS lumbarExtensionWeight, sample.timestamp AS timestamp, calories_measurement.value AS calories "
            + " FROM lumbar_extension_training_measurement "
            + " INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " LEFT JOIN calories_measurement ON lumbar_extension_training_measurement.sample_id = calories_measurement.sample_id "
            + " INNER JOIN tag ON lumbar_extension_training_measurement.sample_id = tag.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND tag.label = 1 ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getWorkTimeLumbarExtensionTraining(LocalDate from, LocalDate to);

    @Query("SELECT duration AS lumbarExtensionDuration, score AS lumbarExtensionScore, repetitions AS lumbarExtensionRepetitions, "
            + " weight AS lumbarExtensionWeight, sample.timestamp AS timestamp, calories_measurement.value AS calories "
            + " FROM lumbar_extension_training_measurement "
            + " INNER JOIN sample ON lumbar_extension_training_measurement.sample_id = sample.id "
            + " LEFT JOIN calories_measurement ON lumbar_extension_training_measurement.sample_id = calories_measurement.sample_id "
            + " WHERE sample.timestamp >= :from AND sample.timestamp < :to AND NOT EXISTS "
            + " (SELECT * FROM tag WHERE tag.sample_id = lumbar_extension_training_measurement.sample_id "
            + " AND tag.label = 1) ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getNotWorkTimeLumbarExtensionTraining(LocalDate from, LocalDate to);

    @Query("SELECT posture_measurement.classification AS postureClassification, SUM(posture_measurement.duration) AS postureDuration FROM posture_measurement INNER JOIN sample ON posture_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to GROUP BY classification")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getPosture(LocalDate from, LocalDate to);

    @Query("SELECT MAX(value) AS steps FROM steps_snapshot_measurement INNER JOIN sample ON steps_snapshot_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getSteps(LocalDate from, LocalDate to);

    @Query("SELECT MAX(value) AS calories, sample.id AS id FROM calories_snapshot_measurement INNER JOIN sample ON calories_snapshot_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ")
    @TypeConverters(EpochTypeConverter.class)
    List<ReportUtil> getSampleID(LocalDate from, LocalDate to);
}
