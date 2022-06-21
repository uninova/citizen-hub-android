package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.entity.EnabledMeasurementRecord;

@Dao
public interface EnabledMeasurementDao {

    @Delete
    void delete(EnabledMeasurementRecord record);

    @Query("DELETE FROM enabled_measurement WHERE device_id = :deviceId")
    void delete(Long deviceId);

    @Query("DELETE FROM enabled_measurement WHERE device_id = (SELECT id FROM device WHERE address = :deviceAddress) AND measurement_type = :measurementType")
    void delete(String deviceAddress, Integer measurementType);

    @Insert
    long insert(EnabledMeasurementRecord record);

    @Query("INSERT OR IGNORE INTO enabled_measurement(device_id, measurement_type) VALUES ((SELECT id FROM device WHERE address = :deviceAddress), :measurementType)")
    long insert(String deviceAddress, Integer measurementType);

    @Query("SELECT * FROM enabled_measurement WHERE device_id = :deviceId")
    List<EnabledMeasurementRecord> select(Long deviceId);

    @Query("SELECT * FROM enabled_measurement WHERE device_id = (SELECT id FROM device WHERE address = :deviceAddress)")
    List<EnabledMeasurementRecord> select(String deviceAddress);

    @Update
    void update(EnabledMeasurementRecord record);

    @Query("SELECT * FROM enabled_measurement")
    LiveData<List<EnabledMeasurementRecord>> selectLiveData();

}