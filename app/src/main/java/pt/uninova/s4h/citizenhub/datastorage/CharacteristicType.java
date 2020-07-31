package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "type")
public class CharacteristicType {
    @PrimaryKey
    private int id;
    @TypeConverters(CharacteristicTypeConverter.class)
    private String name;

    public CharacteristicType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}