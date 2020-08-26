package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DailySummaryDao {

    @Query("SELECT * FROM daily_summary WHERE date = strftime('%s', date('now'))")
    DailySummary get();

    @Query("SELECT * FROM daily_summary WHERE date = strftime('%s', date('now'))")
    LiveData<DailySummary> getLive();

    @Query("SELECT * FROM daily_summary WHERE CAST(strftime('%Y', date, 'unixepoch') AS INT) = :year AND CAST(strftime('%m', date, 'unixepoch') AS INT) = :month AND CAST(strftime('%d', date, 'unixepoch') AS INT) = :day")
    DailySummary getSpecific(Integer year, Integer month, Integer day);

    @Query("SELECT CAST(strftime('%d', date, 'unixepoch') AS INT) AS day FROM daily_summary WHERE CAST(strftime('%Y', date, 'unixepoch') AS INT) = :year AND CAST(strftime('%m', date, 'unixepoch') AS INT) = :month")
    List<Integer> getDaysWithSummaryInYearMonth(Integer year, Integer month);

}
