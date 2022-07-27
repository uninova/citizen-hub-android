package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.entity.StreamRecord;

@Dao
public interface EnabledMeasurementDao {

    @Delete
    void delete(StreamRecord record);

    @Query("DELETE FROM stream WHERE device_id = :deviceId")
    void delete(Long deviceId);

    @Query("DELETE FROM stream WHERE device_id = (SELECT id FROM device WHERE address = :deviceAddress) AND measurement_type = :measurementType")
    void delete(String deviceAddress, Integer measurementType);

    @Insert
    long insert(StreamRecord record);

    @Query("INSERT OR IGNORE INTO stream(device_id, measurement_type) VALUES ((SELECT id FROM device WHERE address = :deviceAddress), :measurementType)")
    long insert(String deviceAddress, Integer measurementType);

    @Query("SELECT * FROM stream WHERE device_id = :deviceId")
    List<StreamRecord> select(Long deviceId);

    @Query("SELECT * FROM stream WHERE device_id = (SELECT id FROM device WHERE address = :deviceAddress)")
    List<StreamRecord> select(String deviceAddress);

    @Update
    void update(StreamRecord record);

    @Query("SELECT * FROM stream")
    LiveData<List<StreamRecord>> selectLiveData();

}