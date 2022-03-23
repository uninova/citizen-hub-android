package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.util.time.LocalDateInterval;

@Dao
public interface MeasurementDao {

    @Query("SELECT * FROM measurement")
    List<Measurement> getAll();

    @Query("SELECT * FROM measurement WHERE kind_id = :kind")
    @TypeConverters(MeasurementKindTypeConverter.class)
    List<Measurement> getAll(MeasurementKind kind);

    @Query("SELECT kind_id as measurementKind, AVG(value) as average, COUNT(value) as count, MAX(value) as max, MIN(value) as min, SUM(value) as sum FROM measurement WHERE timestamp >= :from AND timestamp < :to GROUP BY kind_id;")
    @TypeConverters({EpochTypeConverter.class, MeasurementKindTypeConverter.class})
    List<MeasurementAggregate> getAggregate(LocalDate from, LocalDate to);

    @Query("SELECT kind_id as measurementKind, AVG(value) as average, COUNT(value) as count, MAX(value) as max, MIN(value) as min, SUM(value) as sum FROM measurement WHERE timestamp >= :from AND timestamp < :to AND is_working == :workTime GROUP BY kind_id;")
    @TypeConverters({EpochTypeConverter.class, MeasurementKindTypeConverter.class})
    List<MeasurementAggregate> getAggregateWorkTime(LocalDate from, LocalDate to, int workTime);

    @Query("SELECT kind_id as measurementKind, AVG(value) as average, COUNT(value) as count, MAX(value) as max, MIN(value) as min, SUM(value) as sum FROM measurement WHERE timestamp >= :from AND timestamp < :to GROUP BY kind_id;")
    @TypeConverters({EpochTypeConverter.class, MeasurementKindTypeConverter.class})
    LiveData<List<MeasurementAggregate>> getAggregateLive(LocalDate from, LocalDate to);

    @Query("SELECT DISTINCT (timestamp / 86400) * 86400 FROM measurement WHERE timestamp >= :from AND timestamp < :to")
    @TypeConverters(EpochTypeConverter.class)
    List<LocalDate> getDates(LocalDate from, LocalDate to);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Measurement measurement);

}
