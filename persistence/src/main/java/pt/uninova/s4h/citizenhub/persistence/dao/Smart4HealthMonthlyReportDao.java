package pt.uninova.s4h.citizenhub.persistence.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import pt.uninova.s4h.citizenhub.persistence.entity.Smart4HealthMonthlyReportRecord;

@Dao
public interface Smart4HealthMonthlyReportDao {

    @Insert
    long insert(Smart4HealthMonthlyReportRecord record);

    @Query("INSERT OR REPLACE INTO smart4health_monthly_report (year, month, fhir) VALUES (:year, :month, :value);")
    long insertOrReplaceFhir(Integer year, Integer month, Boolean value);

    @Query("INSERT OR REPLACE INTO smart4health_monthly_report (year, month, pdf) VALUES (:year, :month, :value);")
    long insertOrReplacePdf(Integer year, Integer month, Boolean value);

}