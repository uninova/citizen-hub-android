package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface SmartBearUploadDateDao {

    @Query("SELECT DISTINCT (timestamp / 86400) * 86400 FROM measurement WHERE kind_id = :measurementKind EXCEPT SELECT date FROM smart_bear_upload_date")
    @TypeConverters({EpochTypeConverter.class, MeasurementKindTypeConverter.class})
    List<LocalDate> getDaysWithValues(MeasurementKind measurementKind);

    @Insert
    void insert(SmartBearUploadDateRecord record);
}
