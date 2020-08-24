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

    //TODO
    @Query("SELECT * FROM daily_summary WHERE date = :date")
    @TypeConverters(TimestampConverter.class)
    LiveData<DailySummary> getDailySummary(Date date);

    @Query("select strftime('%d',datetime(date, 'unixepoch')) AS day from date_measurement \n" +
            "where strftime('%Y',datetime(date, 'unixepoch')) = :year \n" +
            "and strftime('%m',datetime(date, 'unixepoch')) = :month;")
    LiveData<List<Integer>> getDaysWithDataInMonthYear(Integer year, Integer month);

}
