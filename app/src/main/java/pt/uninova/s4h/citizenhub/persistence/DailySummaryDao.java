package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Dao
public interface DailySummaryDao {

    @Query("SELECT * FROM daily_summary WHERE date = strftime('%s', date('now'))")
    LiveData<DailySummary> getCurrentSummary();

    @Query("SELECT * FROM daily_summary WHERE date = :date")
    @TypeConverters(TimestampConverter.class)
    LiveData<DailySummary> getDailySummary(Date date);

    @Query("SELECT * FROM daily_summary WHERE strftime('%Y',datetime(date, 'unixepoch')) = :year AND strftime('%m',datetime(date, 'unixepoch')) = :month AND strftime('%d',datetime(date, 'unixepoch')) = :day ")
    LiveData<DailySummary> getDailySummary(Integer year, Integer month, Integer day);

    @Query("SELECT strftime('%d',datetime(date, 'unixepoch')) AS day FROM date_measurement \n" +
            "WHERE strftime('%Y',datetime(date, 'unixepoch')) = :year \n" +
            "AND strftime('%m',datetime(date, 'unixepoch')) = :month;")
    LiveData<List<Integer>> getDaysWithDataInMonthYear(Integer year, Integer month);

}
