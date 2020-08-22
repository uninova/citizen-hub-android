package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView(value = "SELECT DISTINCT date, (SELECT AVG(value) FROM date_measurement WHERE date_measurement.kind_id = 1 AND date_measurement.date = main.date GROUP BY date) average_heart_rate, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 2 AND date_measurement.date = main.date GROUP BY date) sum_steps, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 3 AND date_measurement.date = main.date GROUP BY date) sum_distance, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 4 AND date_measurement.date = main.date GROUP BY date) sum_calories, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 5 AND date_measurement.date = main.date GROUP BY date) sum_good_posture, (SELECT SUM(value) FROM date_measurement WHERE date_measurement.kind_id = 6 AND date_measurement.date = main.date GROUP BY date) sum_bad_posture FROM date_measurement AS main", viewName = "monthly_summary")
public class MonthlySummary {
    @ColumnInfo(name = "month")
    private int month;
    @ColumnInfo(name = "daysWithReport")
    private int day;
    @TypeConverters(TimestampConverter.class)
    private Date date;
}
