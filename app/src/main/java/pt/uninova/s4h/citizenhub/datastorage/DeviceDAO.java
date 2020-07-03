package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeviceDAO {

    @Insert
    String addDevice(Device device);

    @Update
    void updateDevice(Device device);

    @Delete
    void deleteDevice(Device device);

    @Query("DELETE FROM devices")
    void deleteAllDevices();

    @Query("select * from devices")
    List<Device> getDevices();

    @Query("select * from devices where address ==:deviceAddress")
    Device getDevice(String deviceAddress);

}
