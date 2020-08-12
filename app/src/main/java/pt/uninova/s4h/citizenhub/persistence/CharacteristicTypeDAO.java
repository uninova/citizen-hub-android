package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.*;


@Dao
public interface CharacteristicTypeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addType(CharacteristicType type);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateType(CharacteristicType type);

    @Delete
    void deleteType(CharacteristicType type);

}

