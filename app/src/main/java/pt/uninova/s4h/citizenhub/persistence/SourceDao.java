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
public interface SourceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addSource(Source source);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSource(Source source);

    @Delete
    void deleteSource(Source source);

    @Query("DELETE FROM source")
    void deleteAllSources();

    @Query("SELECT * FROM source")
    LiveData<List<Source>> getSources();

    @Query("SELECT * FROM source WHERE uuid =:sourceUuid")
    LiveData<List<Source>> getSourceFromUUID(String sourceUuid);

    @Query("SELECT * FROM source WHERE address =:deviceAddress")
    LiveData<List<Source>> getSourceFomAddress(String deviceAddress);
}
