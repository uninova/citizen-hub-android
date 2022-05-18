package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.BloodPressureMeasurementRecord;

@Dao
public interface BloodPressureMeasurementDao {

    @Query(value = "SELECT blood_pressure_measurement.* FROM blood_pressure_measurement INNER JOIN sample ON blood_pressure_measurement.sample_id = sample.id WHERE sample.timestamp >= :from AND sample.timestamp < :to ORDER BY timestamp")
    @TypeConverters(EpochTypeConverter.class)
    LiveData<List<BloodPressureMeasurementRecord>> get(LocalDate from, LocalDate to);

    @Insert
    void insert(BloodPressureMeasurementRecord record);

    @Query("INSERT INTO blood_pressure_measurement (sample_id, systolic, diastolic, mean_arterial_pressure) VALUES (:sampleId, :systolic, :diastolic, :meanArterialPressure)")
    void insert(Long sampleId, Double systolic, Double diastolic, Double meanArterialPressure);
}
