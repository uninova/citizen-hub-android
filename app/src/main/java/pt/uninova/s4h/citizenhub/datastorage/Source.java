package pt.uninova.s4h.citizenhub.datastorage;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "sources")
public class Source {

    @PrimaryKey
    @NonNull
    private String uuid;
    //CASCADE = "if user row will be deleted, we’d like to delete also all of it repositories"
    @ForeignKey(entity = Device.class, parentColumns = "address", childColumns = "address", onDelete = CASCADE)
    private String address;
    private String type;
    private String interval;

    @Ignore
    public Source() {
    }

    public Source(String uuid, String address, String type, String interval) {
        this.uuid = uuid;
        this.address = address;
        this.type = type;
        this.interval = interval;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}