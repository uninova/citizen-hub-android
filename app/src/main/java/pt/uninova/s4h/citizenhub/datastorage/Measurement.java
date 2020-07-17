package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "measurements")
public class Measurement {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ForeignKey(entity = Source.class, parentColumns = "uuid", childColumns = "uuid", onDelete = CASCADE)
    private String uuid;
    @TypeConverters({Converters.class})
    private Date timestamp;
    private String value;
    private String name;

    @Ignore
    public Measurement() {

    }


    public Measurement(Integer id, String uuid, Date timestamp, String value, String name) {
        this.id = id;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.value = value;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}