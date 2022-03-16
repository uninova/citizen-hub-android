package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(tableName = "sample", indices = @Index(value = {"id"}, unique = true))
public class Sample {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private final Instant timestamp;
    private final String device_address;


    public Sample(Integer id, Instant timestamp, String device_address) {
        this.id = id;
        this.timestamp = timestamp;
        this.device_address = device_address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getDevice_address() {
        return device_address;
    }
}
