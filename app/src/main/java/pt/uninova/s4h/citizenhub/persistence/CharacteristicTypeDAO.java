package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;


@Dao
public interface CharacteristicTypeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addType(CharacteristicType type);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateType(CharacteristicType type);

    @Delete
    void deleteType(CharacteristicType type);

}

