package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.entity.EnabledMeasurement;

@Dao
public interface FeatureDao {

    @Delete
    void delete(EnabledMeasurement enabledMeasurement);

    @Query("DELETE FROM EnabledMeasurement WHERE device_address = :address")
    void deleteAll(String address);

    @Query("SELECT * FROM EnabledMeasurement")
    LiveData<List<EnabledMeasurement>> get();

    @Insert
    void insert(EnabledMeasurement enabledMeasurement);

    @Query("SELECT * FROM EnabledMeasurement WHERE device_address = :address")
    List<EnabledMeasurement> select(String address);

    @Query("SELECT kind_id as measurementKind FROM EnabledMeasurement WHERE device_address = :address")
    List<Integer> selectType(String address);

    @Update
    void update(EnabledMeasurement enabledMeasurement);

}
