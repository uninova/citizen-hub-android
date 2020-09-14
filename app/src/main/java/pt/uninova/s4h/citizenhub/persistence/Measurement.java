package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.*;

import java.util.Date;

@Entity(tableName = "measurement")
public class Measurement {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @TypeConverters({EpochTypeConverter.class})
    private Date timestamp;
    @ColumnInfo(name = "kind_id")
    @TypeConverters(MeasurementKindTypeConverter.class)
    private MeasurementKind kind;
    private Double value;

    @Ignore
    public Measurement(Date timestamp, MeasurementKind kind, Double value) {
        this(null, timestamp, kind, value);
    }

    public Measurement(Integer id, Date timestamp, MeasurementKind kind, Double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.kind = kind;
        this.value = value;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}