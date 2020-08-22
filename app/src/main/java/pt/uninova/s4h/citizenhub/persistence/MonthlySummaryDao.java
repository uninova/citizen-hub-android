package pt.uninova.s4h.citizenhub.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.Date;

@Dao
public interface MonthlySummaryDao {
    @Query("SELECT * FROM monthly_summary WHERE date = strftime('%s', date(month))")
    LiveData<MonthlySummary> getMonthlySummary(Date month);

    @Query("SELECT * FROM daily_summary WHERE date = :month")
    @TypeConverters(TimestampConverter.class)
    LiveData<MonthlySummary> getMonthSummary(Date month);
}
