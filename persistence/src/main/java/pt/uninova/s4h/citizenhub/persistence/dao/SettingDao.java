package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import pt.uninova.s4h.citizenhub.persistence.entity.SettingRecord;

@Dao
public interface SettingDao {

    @Delete
    void delete(SettingRecord record);

    @Insert
    long insert(SettingRecord record);

    @Update
    void update(SettingRecord record);
}