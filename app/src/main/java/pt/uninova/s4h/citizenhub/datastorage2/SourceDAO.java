package pt.uninova.s4h.citizenhub.datastorage2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SourceDAO {

    @Insert
    String addSource(Source source);

    @Update
    void updateSource(Source source);

    @Delete
    void deleteSource(Source source);

    @Query("select * from sources")
    List<Source> getSources();


    @Query("select * from sources where uuid ==:sourceUuid")
    Device getSourceFromUUID(String sourceUuid);

    @Query("select * from sources where address ==:deviceAddress")
    Device getSourceFomAddress(String deviceAddress);
}
