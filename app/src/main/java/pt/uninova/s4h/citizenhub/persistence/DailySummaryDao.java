package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;
import pt.uninova.util.Pair;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface DailySummaryDao {

    @Query("SELECT * FROM daily_summary WHERE date = strftime('%s', date('now'))")
    DailySummary getCurrent();

    @Query("SELECT * FROM daily_summary WHERE date = strftime('%s', date('now'))")
    LiveData<DailySummary> getCurrentLive();

    @Query("SELECT * FROM daily_summary WHERE CAST(strftime('%Y', date, 'unixepoch') AS INT) = :year AND CAST(strftime('%m', date, 'unixepoch') AS INT) = :month AND CAST(strftime('%d', date, 'unixepoch') AS INT) = :day")
    DailySummary get(Integer year, Integer month, Integer day);

    @Query("SELECT DISTINCT date FROM daily_summary WHERE CAST(strftime('%Y', date, 'unixepoch') AS INT) = :year AND CAST(strftime('%m', date, 'unixepoch') AS INT) = :month")
    @TypeConverters(TimestampConverter.class)
    List<LocalDate> getDaysWithSummaryInYearMonth(Integer year, Integer month);

    @Query("SELECT DISTINCT date FROM date_measurement WHERE date >= :from AND date < :to")
    @TypeConverters(TimestampConverter.class)
    List<LocalDate> getAvailableReportDates(Pair<Integer, Integer> from, Pair<Integer, Integer> to);

    @Query("SELECT MIN(date) FROM date_measurement")
    @TypeConverters(TimestampConverter.class)
    LocalDate getEarliestAvailableReportDate();

    @Query("SELECT MAX(date) FROM date_measurement")
    @TypeConverters(TimestampConverter.class)
    LiveData<LocalDate> getLatestAvailableReportDateLive();

}
