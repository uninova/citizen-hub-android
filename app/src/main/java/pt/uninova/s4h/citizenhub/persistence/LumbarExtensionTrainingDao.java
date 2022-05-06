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

    @Delete
    void delete(LumbarExtensionTrainingRecord record);

    @Query("DELETE FROM lumbar_extension_training_measurement")
    void deleteAll();

    @Query(value = "SELECT * FROM lumbar_extension_training_measurement WHERE timestamp >= :from AND timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<LumbarExtensionTrainingRecord>> get(LocalDate from, LocalDate to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LumbarExtensionTrainingRecord record);

    @Query(value = "SELECT * FROM lumbar_extension_training_measurement WHERE timestamp >= :from AND timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    List<LumbarExtensionTrainingRecord> select(LocalDate from, LocalDate to);

    @Query("SELECT * FROM lumbar_extension_training_measurement")
    List<LumbarExtensionTrainingRecord> selectAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LumbarExtensionTrainingRecord record);
}
