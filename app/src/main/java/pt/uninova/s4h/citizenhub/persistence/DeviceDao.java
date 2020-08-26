package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addDevice(Device device);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDevice(Device device);

    @Delete
    void deleteDevice(Device device);

    @Query("DELETE FROM device")
    void deleteAllDevices();

    @Query("SELECT * FROM device")
    LiveData<List<Device>> getDevices();

    @Query("SELECT * FROM device WHERE address =:deviceAddress")
    LiveData<Device> getDevice(String deviceAddress);

}
