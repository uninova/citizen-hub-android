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

    @Query("SELECT * FROM lumbar_training WHERE timestamp >= :from AND timestamp < :to")
    @TypeConverters({EpochTypeConverter.class, MeasurementKindTypeConverter.class})
    LiveData<List<LumbarAggregate>> getAggregateLive(LocalDate from, LocalDate to);
}
