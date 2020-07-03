package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MeasurementDAO {

    @Insert
    String addMeasurement(Measurement measurement);

    @Update
    void updateMeasurement(Measurement measurement);

    @Delete
    void deleteMeasurement(Measurement measurement);

    @Query("DELETE FROM measurements")
    void deleteAllMeasurements();


    @Query("select * from measurements")
    List<Measurement> getMeasurements();

    @Query("SELECT AVG(value) FROM measurements WHERE name = :columnName AND timestamp =:date ")
    int getAvgFromDay(String columnName, String date);

    @Query("SELECT SUM(value) FROM measurements WHERE name = :columnName AND timestamp=:date ")
    int getSumFromDay(String columnName, String date);

    @Query("SELECT AVG(value) FROM measurements WHERE name = :columnName  AND timestamp BETWEEN  :timestamp_start AND :timestamp_end")
    int getAvgFromDayInterval(String columnName, String timestamp_start, String timestamp_end);

    @Query("SELECT SUM(value) FROM measurements WHERE name = :columnName  AND timestamp BETWEEN  :timestamp_start AND :timestamp_end")
    int getSumFromDayInterval(String columnName, String timestamp_start, String timestamp_end);


}
