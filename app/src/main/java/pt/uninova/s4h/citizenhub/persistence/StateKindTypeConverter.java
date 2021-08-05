package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;

public class StateKindTypeConverter {

    @TypeConverter
    public static StateKind fromInt(int id) {
        return StateKind.find(id);
    }

    @TypeConverter
    public static int toInt(StateKind stateKind) {
        return stateKind.getId();
    }

}