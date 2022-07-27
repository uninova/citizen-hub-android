package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import pt.uninova.s4h.citizenhub.persistence.entity.Smart4HealthDailyReportRecord;

@Dao
public interface Smart4HealthDailyReportDao {

    @Insert
    long insert(Smart4HealthDailyReportRecord record);

}
