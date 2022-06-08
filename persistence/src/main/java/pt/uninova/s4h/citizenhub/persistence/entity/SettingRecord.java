package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "setting",
        primaryKeys = {"device_id", "key"},
        foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE, entity = DeviceRecord.class, parentColumns = "id", childColumns = "device_id")
)
public class SettingRecord {

    @NonNull
    @ColumnInfo(name = "device_id")
    private Long deviceId;
    @NonNull
    private String key;
    private String value;

    public SettingRecord(Long deviceId, String key, String value) {
        this.deviceId = deviceId;
        this.key = key;
        this.value = value;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
