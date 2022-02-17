package persistence;

import java.sql.Timestamp;

public class Measurement {
    private int id;
    private Double value;
    private Boolean isSent;
    //@ColumnInfo(name = "kind_id")
    // @TypeConverters(MeasurementKindTypeConverter.class)
    private String kind;
    private Timestamp timestamp;


    public Measurement(int id, String kind, Double value, Boolean isSent, Timestamp timestamp) {
        this.id = id;
        this.kind = kind;
        this.value = value;
        this.isSent = isSent;
        this.timestamp = timestamp;
    }

    public Measurement() {
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", kind='" + kind +
                ", value=" + value +
                ", isSent=" + isSent +
                ", timestamp=" + timestamp + '\'' +
                '}';
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}