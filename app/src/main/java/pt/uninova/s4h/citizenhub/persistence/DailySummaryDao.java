package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.Date;

@Dao
public interface DailySummaryDao {

    @Query("SELECT * FROM daily_summary WHERE date = strftime('%s', date('now'))")
    LiveData<DailySummary> getCurrentSummary();

    @Query("SELECT * FROM daily_summary WHERE date = :date")
    @TypeConverters(TimestampConverter.class)
    LiveData<DailySummary> getDailySummary(Date date);

}
