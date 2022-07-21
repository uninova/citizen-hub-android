package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.entity.DeviceRecord;

@Dao
public interface DeviceDao {

    @Query("DELETE FROM device")
    void delete();

    @Delete
    void delete(DeviceRecord record);

    @Query("DELETE FROM device WHERE address = :address")
    void delete(String address);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DeviceRecord record);

    @Query("SELECT * FROM device")
    List<DeviceRecord> select();

    @Query("SELECT * FROM device WHERE id = :deviceId")
    DeviceRecord select(Long deviceId);

    @Query("SELECT * FROM device WHERE address = :address")
    DeviceRecord select(String address);

    @Update()
    void update(DeviceRecord record);

    @Query("UPDATE device SET agent = :agent WHERE address = :address")
    void updateAgent(String address, String agent);

}