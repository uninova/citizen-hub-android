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

    @Query("SELECT AVG(value) FROM measurements WHERE name = :characteristicName AND timestamp=datetime(:date) ")
    int getAvgFromDay(String characteristicName, Date date);

    //TODO databaseView sum/avg

    //datawarehouse
    @Query("SELECT SUM(value) FROM measurements WHERE name = :characteristicName AND timestamp=datetime(:date) ")
    long getSumFromDay(String characteristicName, Date date);


    @Query("SELECT AVG(value) FROM measurements WHERE name = :characteristicName  AND timestamp BETWEEN  datetime(:timestamp_start) AND datetime(:timestamp_end)")
    long getAvgFromDayInterval(String characteristicName, Date timestamp_start, Date timestamp_end);

    @Query("SELECT SUM(value) FROM measurements WHERE name = :characteristicName  AND timestamp BETWEEN  datetime(:timestamp_start) AND datetime(:timestamp_end)")
    long getSumFromDayInterval(String characteristicName, Date timestamp_start, Date timestamp_end);


}
