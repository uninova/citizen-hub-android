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

    @Query("SELECT * FROM daily_summary WHERE CAST(strftime('%Y', date, 'unixepoch') AS INT) = :year AND CAST(strftime('%m', date, 'unixepoch') AS INT) = :month AND CAST(strftime('%d', date, 'unixepoch') AS INT) = :day")
    LiveData<DailySummary> getDailySummary(Integer year, Integer month, Integer day);

    @Query("SELECT CAST(strftime('%d', date, 'unixepoch') AS INT) AS day FROM daily_summary WHERE CAST(strftime('%Y', date, 'unixepoch') AS INT) = :year AND CAST(strftime('%m', date, 'unixepoch') AS INT) = :month")
    LiveData<List<Integer>> getDaysWithSummaryInYearMonth(Integer year, Integer month);

}
