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
public interface DeviceDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addDevice(Device device);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDevice(Device device);

    @Delete
    void deleteDevice(Device device);

    @Query("DELETE FROM devices")
    void deleteAllDevices();

    //Sql all in caps
    @Query("SELECT * FROM devices")
    LiveData<List<Device>> getDevices();

    @Query("SELECT * FROM devices WHERE address ==:deviceAddress")
    Device getDevice(String deviceAddress);

}
