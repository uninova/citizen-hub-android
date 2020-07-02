package pt.uninova.s4h.citizenhub.datastorage2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface DeviceDAO {

    @Insert
    String addDevice(Device device);

    @Update
    void updateDevice(Device device);

    @Delete
    void deleteDevice(Device device);


}
