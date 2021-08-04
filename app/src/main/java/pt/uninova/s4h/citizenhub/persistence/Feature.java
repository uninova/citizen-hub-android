package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "feature", primaryKeys = {"device_address", "kind_id"}, foreignKeys = @ForeignKey(onDelete = CASCADE, entity = Device.class, parentColumns = "address", childColumns = "device_address"))
public class Feature {
    @NonNull
    private String device_address;
    @ColumnInfo(name = "kind_id")
    @TypeConverters(MeasurementKindTypeConverter.class)
    @NonNull
    private MeasurementKind kind;

    @Ignore
    public Feature() {
        device_address = null;
        kind = null;
    }

    public Feature(@NotNull String device_address, @NotNull MeasurementKind kind) {
        this.device_address = device_address;
        this.kind = kind;
    }

    @NotNull
    public MeasurementKind getKind() {
        return kind;
    }

    public void setKind(@NotNull MeasurementKind kind) {
        this.kind = kind;
    }

    @NotNull
    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = this.device_address;
    }
}