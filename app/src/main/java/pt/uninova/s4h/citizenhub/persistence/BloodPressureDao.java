package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

@Dao
public interface BloodPressureDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BloodPressureMeasurement bloodPressureMeasurement);

}
