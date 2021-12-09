package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.util.time.LocalDateInterval;

@Dao
public interface LumbarExtensionTrainingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LumbarExtensionTraining lumbarExtensionTraining);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LumbarExtensionTraining lumbarExtensionTraining);

    @Delete
    void delete(LumbarExtensionTraining lumbarExtensionTraining);

    @Query("DELETE FROM lumbar_training")
    void deleteAll();

    @Query("SELECT * FROM lumbar_training")
    List<LumbarExtensionTraining> getAll();

    @Query(value = "SELECT * FROM lumbar_training WHERE timestamp >= :from AND timestamp < :to")
    @TypeConverters({EpochTypeConverter.class, MeasurementKindTypeConverter.class})
    LiveData<LumbarExtensionTraining> getLumbarTraining(LocalDate from, LocalDate to);


    @Query("SELECT MIN(DATE(timestamp)) AS lower, MAX(DATE(timestamp)) AS upper FROM lumbar_training;")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<LocalDateInterval> getDateBoundsLive();

    @Query("SELECT DISTINCT (timestamp / 86400) * 86400 FROM lumbar_training WHERE DATE(timestamp) >= :from AND DATE(timestamp) < :to")
    @TypeConverters(EpochTypeConverter.class)
    List<LocalDate> getDates(LocalDate from, LocalDate to);

}
