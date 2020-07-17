package pt.uninova.s4h.citizenhub.datastorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT AVG(value) FROM measurements WHERE name = :characteristicName AND timestamp=date(:date) ")
    int getAvgFromDay(String characteristicName, String date);

    //TODO databaseView sum/avg

    //datawarehouse
    @Query("SELECT SUM(value) FROM measurements WHERE name = :characteristicName AND timestamp=date(:date) ")
    long getSumFromDay(String characteristicName, String date);


    @Query("SELECT AVG(value) FROM measurements WHERE name = :characteristicName  AND timestamp BETWEEN  date(:timestamp_start) AND date(:timestamp_end)")
    long getAvgFromDayInterval(String characteristicName, String timestamp_start, String timestamp_end);

    @Query("SELECT SUM(value) FROM measurements WHERE name = :characteristicName  AND timestamp BETWEEN  date(:timestamp_start) AND date(:timestamp_end)")
    long getSumFromDayInterval(String characteristicName, String timestamp_start, String timestamp_end);


}
