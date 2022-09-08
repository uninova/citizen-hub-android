package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.Smart4HealthDailyReportRecord;

@Dao
public interface Smart4HealthDailyReportDao {

    @Insert
    long insert(Smart4HealthDailyReportRecord record);

    @Query("INSERT OR REPLACE INTO smart4health_daily_report (date, fhir) VALUES (:date, :value);")
    @TypeConverters(EpochTypeConverter.class)
    long insertOrReplaceFhir(LocalDate date, Boolean value);

    @Query("INSERT OR REPLACE INTO smart4health_daily_report (date, pdf) VALUES (:date, :value);")
    @TypeConverters(EpochTypeConverter.class)
    long insertOrReplacePdf(LocalDate date, Boolean value);

    @Query("INSERT OR REPLACE INTO smart4health_daily_report (date, pdf) VALUES (:date, :value);")
    long insertOrReplacePdf(long date, Boolean value);

    @Query("SELECT DISTINCT (timestamp / 86400000) * 86400000 FROM posture_measurement LEFT JOIN sample ON posture_measurement.sample_id = sample_id EXCEPT SELECT date FROM smart4health_daily_report")
    @TypeConverters(EpochTypeConverter.class)
    List<LocalDate> selectDaysWithValues();

    @Query("SELECT DISTINCT (timestamp + (:offset * 3600000) / 86400000) * 86400000 FROM posture_measurement LEFT JOIN sample ON posture_measurement.sample_id = sample_id EXCEPT SELECT date FROM smart4health_daily_report")
    @TypeConverters(EpochTypeConverter.class)
    List<LocalDate> selectDaysWithValues(int offset);

}
