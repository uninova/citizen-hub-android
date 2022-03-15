package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

@Dao
public interface BloodPressureDao {

    @Query("SELECT * FROM blood_pressure")
    List<BloodPressureRecord> getAll();

    @Query("SELECT * FROM blood_pressure WHERE kind_id = :kind")
    @TypeConverters(MeasurementKindTypeConverter.class)
    List<BloodPressureRecord> getAll(MeasurementKind kind);

    @Query("SELECT * FROM blood_pressure WHERE kind_id = :kind ORDER BY timestamp DESC LIMIT 1")
    @TypeConverters(MeasurementKindTypeConverter.class)
    List<BloodPressureRecord> getLastRecord(MeasurementKind kind);

    //getLastSystolic in repository?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BloodPressureRecord bloodPressureRecord);

}
