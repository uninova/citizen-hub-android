package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.*;

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
    LiveData<Device> getDevice(String deviceAddress);

}
