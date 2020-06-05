package pt.uninova.s4h.citizenhub.storage;

public class DeviceRecord {
    private Integer id;

    private String name;
    private String address;
    private String type;
    private String state;


    public DeviceRecord(Integer id, String name, String address, String type, String state) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.state = state;
    }

    public DeviceRecord(String name, String address, String type, String state) {
        this(null, name, address, type, state);
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }


}

