package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import pt.uninova.s4h.citizenhub.persistence.entity.Smart4HealthWeeklyReportRecord;

@Dao
public interface Smart4HealthWeeklyReportDao {

    @Insert
    long insert(Smart4HealthWeeklyReportRecord record);

    @Query("INSERT OR REPLACE INTO smart4health_weekly_report (year, week, fhir) VALUES (:year, :week, :value);")
    long insertOrReplaceFhir(Integer year, Integer week, Boolean value);

    @Query("INSERT OR REPLACE INTO smart4health_weekly_report (year, week, pdf) VALUES (:year, :week, :value);")
    long insertOrReplacePdf(Integer year, Integer week, Boolean value);
    
}