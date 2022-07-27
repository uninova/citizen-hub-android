package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.conversion.EpochTypeConverter;
import pt.uninova.s4h.citizenhub.persistence.entity.SmartBearDailyReportRecord;

@Dao
public interface SmartBearDailyReportDao {

    @Insert
    long insert(SmartBearDailyReportRecord record);

    @Query("SELECT DISTINCT (timestamp / 86400) * 86400 FROM posture_measurement LEFT JOIN sample ON posture_measurement.sample_id = sample_id EXCEPT SELECT date FROM smart_bear_daily_report")
    @TypeConverters(EpochTypeConverter.class)
    List<LocalDate> selectDaysWithValues();

}