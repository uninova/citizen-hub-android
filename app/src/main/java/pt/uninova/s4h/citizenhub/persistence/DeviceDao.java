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
    void insert(DeviceRecord deviceRecord);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DeviceRecord deviceRecord);

    @Delete
    void delete(DeviceRecord deviceRecord);

    @Query("DELETE FROM device")
    void deleteAll();

    @Query("SELECT * FROM device")
    List<DeviceRecord> getAll();

    @Query("SELECT * FROM device WHERE address =:deviceAddress")
    DeviceRecord get(String deviceAddress);


    @Query("SELECT * FROM device WHERE connection_kind=:connectionKind")
    @TypeConverters(ConnectionKindTypeConverter.class)
    List<DeviceRecord> getAllWithConnectionKind(ConnectionKind connectionKind);

    @Query("SELECT * FROM device WHERE state=:stateKind")
    @TypeConverters(StateKindTypeConverter.class)
    List<DeviceRecord> getWithState(StateKind stateKind);

    @Query("SELECT * FROM device WHERE type=:type")
    List<DeviceRecord> getWithAgent(String type);

}
