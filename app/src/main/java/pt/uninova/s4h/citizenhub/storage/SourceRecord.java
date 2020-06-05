package pt.uninova.s4h.citizenhub.storage;

public class SourceRecord {
    private Integer id;
    private String uuid;
    private String address;
    private String type;
    private String interval;

    public SourceRecord(Integer id, String uuid, String address, String type, String interval) {
        this.id = id;
        this.uuid = uuid;
        this.address = address;
        this.type = type;
        this.interval = interval;
    }

    public SourceRecord(String uuid, String address, String type, String interval) {
        this(null, uuid, address, type, interval);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
