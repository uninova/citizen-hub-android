package pt.uninova.s4h.citizenhub.storage;

public class MeasurementsRecord {
    private Integer id;
    private String uuid;
    private String timestamp;
    private String value;
    private String name;

    public MeasurementsRecord(Integer id, String uuid, String timestamp, String value, String name) {
        this.id = id;
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.value = value;
        this.name = name;
    }

    public MeasurementsRecord(String uuid, String timestamp, String value, String name) {
        this(null, uuid, timestamp, value, name);
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
