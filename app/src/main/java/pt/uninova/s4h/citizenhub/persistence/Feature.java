package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "feature", foreignKeys = @ForeignKey(entity = Device.class, parentColumns = "address", childColumns = "device_address")
)
public class Feature {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String device_address;
    private String uuid;
    private int state; // TODO enum e typeconverter

    @Ignore
    public Feature() {
    }

    public Feature(int id, String device_address, String uuid) {
        this.id = id;
        this.device_address = device_address;
        // this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = this.device_address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}