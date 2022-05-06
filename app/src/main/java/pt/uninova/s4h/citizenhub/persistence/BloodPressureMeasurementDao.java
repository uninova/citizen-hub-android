package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface BloodPressureMeasurementDao {

    @Query(value = "SELECT blood_pressure_measurement.* FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<BloodPressureMeasurementRecord>> get(LocalDate from, LocalDate to);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BloodPressureMeasurementRecord bloodPressureMeasurementRecord);

}
