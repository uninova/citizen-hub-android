package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView(value = "SELECT CAST(strftime('%m', datetime(date, 'unixepoch')) AS int) AS date from date_measurement WHERE CAST(strftime('%m', datetime(date, 'unixepoch')) AS int) = CAST(strftime('%m', datetime('now', 'unixepoch')) AS int)", viewName = "monthly_summary")
public class MonthlySummary {
    @ColumnInfo(name = "month")
    private int month;
    @ColumnInfo(name = "days_with_report")
    private int day;
    @TypeConverters(TimestampConverter.class)
    private Date date;
}
