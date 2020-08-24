package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView("SELECT AVG(value) AS AverageValue , strftime('%s', strftime('%Y-%m-%d', timestamp, 'UNIXEPOCH')) AS Date, type as characteristicType FROM measurement GROUP BY Date, characteristicType")
public class AverageMeasurement {
    public float AverageValue;
    @TypeConverters({TimestampConverter.class})
    public Date date;
    public int characteristicType;
}
//SELECT strftime('%s', strftime('%Y-%m-%d','now', 'UNIXEPOCH'))


// note - SELECT MONTH with year == ? -> SELECT CAST(strftime('%m', datetime(date, 'unixepoch')) AS int) from date_measurement WHERE CAST(strftime('%Y', datetime(date, 'unixepoch')) AS int) = CAST(strftime('%Y', datetime(1597968000, 'unixepoch')) AS int)
// note - SELECT DAY WITHIN MONTH -> SELECT CAST(strftime('%m', datetime(date, 'unixepoch')) AS int) from date_measurement WHERE CAST(strftime('%m', datetime(date, 'unixepoch')) AS int) = CAST(strftime('%m', datetime(1597968000, 'unixepoch')) AS int)

//working
// YEAR - CAST(strftime('%Y', datetime(date, 'unixepoch')) AS int) AS year
// MONTH - CAST(strftime('%m', datetime(date, 'unixepoch')) AS int) AS month
// 1597968000 example
