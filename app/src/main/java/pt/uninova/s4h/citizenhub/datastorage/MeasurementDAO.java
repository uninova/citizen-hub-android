package pt.uninova.s4h.citizenhub.datastorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface MeasurementDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMeasurement(Measurement measurement);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMeasurement(Measurement measurement);

    @Delete
    void deleteMeasurement(Measurement measurement);

    @Query("DELETE FROM measurements")
    void deleteAllMeasurements();

    @Query("SELECT * FROM measurements")
    LiveData<List<Measurement>> getMeasurements();

    @Query("SELECT * FROM measurements WHERE name = :characteristicName")
    LiveData<List<Measurement>> getMeasurementsWithCharacteristic(String characteristicName);

    @Query("SELECT * FROM AverageMeasurement")
    LiveData<List<AverageMeasurement>> getMeasurementsWithoutTime();

    @Query("SELECT AVG(value) FROM measurements WHERE name = :characteristicName AND timestamp=datetime(:date) ")
    float getAvgFromDay(String characteristicName, Date date);

    @Query("SELECT SUM(value) FROM measurements WHERE name = :characteristicName AND timestamp=datetime(:date) ")
    float getSumFromDay(String characteristicName, Date date);





}
