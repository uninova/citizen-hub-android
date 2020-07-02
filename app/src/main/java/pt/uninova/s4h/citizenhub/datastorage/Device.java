package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "devices")
public class Device {

    private String name;
    @PrimaryKey
    private String address;
    private String type;
    private String state;

    @Ignore
    public Device() {

    }

    public Device(String name, String address, String type, String state) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}