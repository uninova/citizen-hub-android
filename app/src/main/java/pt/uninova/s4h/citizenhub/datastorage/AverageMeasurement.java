package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView("SELECT AVG(value) AS AverageValue , strftime('%s', strftime('%Y-%m-%d', timestamp, 'UNIXEPOCH')) AS Date, type as characteristicType FROM measurements GROUP BY Date, characteristicType")
public class AverageMeasurement {
    public float AverageValue;
    @TypeConverters({TimestampConverter.class})
    public Date date;
    public int characteristicType;
}
//SELECT strftime('%s', strftime('%Y-%m-%d','now', 'UNIXEPOCH'))