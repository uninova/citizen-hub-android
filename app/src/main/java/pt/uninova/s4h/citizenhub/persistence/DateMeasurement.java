package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.TypeConverters;

import java.util.Date;

@DatabaseView(value = "SELECT CAST(strftime('%s', strftime('%Y-%m-%d', timestamp, 'UNIXEPOCH')) AS INT) AS date, id, kind_id, value FROM measurement", viewName = "date_measurement")
public class DateMeasurement {

    @TypeConverters({TimestampConverter.class})
    private Date date;
    private Integer id;
    @ColumnInfo(name = "kind_id")
    @TypeConverters(MeasurementKindTypeConverter.class)
    private MeasurementKind kind;
    private Double value;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MeasurementKind getKind() {
        return kind;
    }

    public void setKind(MeasurementKind kind) {
        this.kind = kind;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
