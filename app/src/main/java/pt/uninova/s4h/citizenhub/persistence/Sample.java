package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

import pt.uninova.s4h.citizenhub.connectivity.Device;

@Entity(tableName = "sample")
public class Sample {
    @PrimaryKey
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
