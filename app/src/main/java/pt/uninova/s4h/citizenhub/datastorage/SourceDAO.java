package pt.uninova.s4h.citizenhub.datastorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
