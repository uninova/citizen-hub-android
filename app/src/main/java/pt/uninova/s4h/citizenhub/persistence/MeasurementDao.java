package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

@Dao
public interface MeasurementDao {

    @Query("SELECT * FROM measurement")
    List<Measurement> getAll();

    @Query("SELECT * FROM measurement WHERE kind_id = :kind")
    @TypeConverters(MeasurementKindTypeConverter.class)
    List<Measurement> get(MeasurementKind kind);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Measurement measurement);

}
