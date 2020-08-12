package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface SourceDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addSource(Source source);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSource(Source source);

    @Delete
    void deleteSource(Source source);

    @Query("DELETE FROM sources")
    void deleteAllSources();

    @Query("SELECT * FROM sources")
    LiveData<List<Source>> getSources();


    @Query("SELECT * FROM sources WHERE uuid ==:sourceUuid")
    LiveData<List<Source>> getSourceFromUUID(String sourceUuid);

    @Query("SELECT * FROM sources WHERE address ==:deviceAddress")
    LiveData<List<Source>> getSourceFomAddress(String deviceAddress);
}
