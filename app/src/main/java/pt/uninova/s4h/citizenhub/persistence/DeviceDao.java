package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeviceDao {

    @Delete
    void delete(DeviceRecord record);

    @Query("DELETE FROM device WHERE address = :address")
    void delete(String address);

    @Query("DELETE FROM device")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DeviceRecord record);

    @Query("SELECT * FROM device WHERE address =:address")
    DeviceRecord select(String address);

    @Query("SELECT * FROM device")
    List<DeviceRecord> selectAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DeviceRecord record);
}
