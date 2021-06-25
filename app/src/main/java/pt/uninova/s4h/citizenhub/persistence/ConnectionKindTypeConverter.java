package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;

public class ConnectionKindTypeConverter {

    @TypeConverter
    public static ConnectionKind fromInt(int id) {
        return ConnectionKind.find(id);
    }

    @TypeConverter
    public static int toInt(ConnectionKind connectionKind) {
        return connectionKind.getId();
    }

}


