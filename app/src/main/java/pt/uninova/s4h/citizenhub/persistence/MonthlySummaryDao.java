package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Dao
public interface MonthlySummaryDao {
    @Query("SELECT CAST(strftime('%d', datetime(date, 'unixepoch')) AS int) AS date from date_measurement WHERE date BETWEEN catchTheFirstDayOfTheMonth(:month) AND catchTheLastDayOfTheMonth(:month)")
    LiveData<List<Integer>> getDaysWithSummary(Integer month);

    @Query("select strftime('%d',datetime(date, 'unixepoch')) from date_measurement \n" +
            "where strftime('%m',datetime(date, 'unixepoch')) = :month \n" +
            "and strftime('%Y',datetime(date, 'unixepoch')) = :year;")
    LiveData<List<Integer>> getDaysWithDataInMonthYear(Integer month, Integer year);
}
