package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.*;

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
    List<Source> getSourceFromUUID(String sourceUuid);

    @Query("SELECT * FROM source WHERE address =:deviceAddress")
    List<Source> getSourceFomAddress(String deviceAddress);
}
