package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
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
    List<Device> getAll();

    @Query("SELECT * FROM device WHERE address =:deviceAddress")
    Device get(String deviceAddress);


    @Query("SELECT * FROM device WHERE connection_kind=:connectionKind")
    @TypeConverters(ConnectionKindTypeConverter.class)
    List<Device> getAllWithConnectionKind(ConnectionKind connectionKind);

    @Query("SELECT * FROM device WHERE state=:stateKind")
    @TypeConverters(StateKindTypeConverter.class)
    List<Device> getWithState(StateKind stateKind);

    @Query("SELECT * FROM device WHERE type=:type")
    List<Device> getWithAgent(String type);

}
