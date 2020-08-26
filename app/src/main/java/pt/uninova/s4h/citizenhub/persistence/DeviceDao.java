package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Device device);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Device device);

    @Delete
    void delete(Device device);

    @Query("DELETE FROM device")
    void deleteAll();

    @Query("SELECT * FROM device")
    LiveData<List<Device>> getAll();

    @Query("SELECT * FROM device WHERE address =:deviceAddress")
    Device get(String deviceAddress);

}
