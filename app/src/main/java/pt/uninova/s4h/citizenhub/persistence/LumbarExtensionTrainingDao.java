package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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


}
