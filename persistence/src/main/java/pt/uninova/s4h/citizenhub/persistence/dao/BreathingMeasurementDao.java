package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.BreathingMeasurementRecord;

@Dao
public interface BreathingMeasurementDao {

    @Insert
    long insert(BreathingMeasurementRecord record);

    @Query("INSERT INTO breathing_measurement (sample_id, `index`, type, value) VALUES (:sampleId, :index, :type, :value)")
    long insert(Long sampleId, Integer index, Integer type, Double value);

    @Query("SELECT breathing_measurement.* FROM breathing_measurement INNER JOIN sample ON breathing_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp ASC, `index` ASC")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<BreathingMeasurementRecord>> selectLiveData(LocalDate from, LocalDate to);

}