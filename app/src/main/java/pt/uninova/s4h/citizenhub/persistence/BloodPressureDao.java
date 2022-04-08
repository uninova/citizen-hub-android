package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface BloodPressureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BloodPressureRecord bloodPressureRecord);

}
