package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeasurementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMeasurement(Measurement measurement);

    @Query("SELECT * FROM measurement")
    List<Measurement> getMeasurements();

    @Query("SELECT * FROM measurement WHERE kind_id = :characteristicType")
    List<Measurement> getMeasurementsWithCharacteristic(int characteristicType);


}
