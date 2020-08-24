package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView(value = "SELECT strftime('%s', strftime('%Y-%m-%d', timestamp, 'UNIXEPOCH')) AS date, id, type as kind_id, value FROM measurement", viewName = "date_measurement")
public class DateMeasurement {

    @TypeConverters({TimestampConverter.class})
    private Date date;
    private int id;
    @ColumnInfo(name = "kind_id")
    @TypeConverters(CharacteristicTypeConverter.class)
    private int kindId;
    private double value;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKindId() {
        return kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
