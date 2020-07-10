package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SourceDAO {

    @Insert
    void addSource(Source source);

    @Update
    void updateSource(Source source);

    @Delete
    void deleteSource(Source source);

    @Query("DELETE FROM sources")
    void deleteAllSources();

    @Query("SELECT * FROM sources")
    List<Source> getSources();


    @Query("SELECT * FROM sources WHERE uuid ==:sourceUuid")
    Device getSourceFromUUID(String sourceUuid);

    @Query("SELECT * FROM sources WHERE address ==:deviceAddress")
    Device getSourceFomAddress(String deviceAddress);
}
