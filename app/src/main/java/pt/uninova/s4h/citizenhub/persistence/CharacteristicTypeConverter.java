package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;

public class CharacteristicTypeConverter{
    @TypeConverter
    public static int fromString(String name) {
        int type;
        switch (name) {
            case "HeartRate":
                type = 0;
                break;
            case "Steps":
                type = 1;
                break;
            case "Posture":
                type=2;
                break;
            default:
                type = -1;
                break;
        }
        return type;
    }


    @TypeConverter
    public static String fromInt(int type) {
        String name;
        switch (type) {
            case 0:
                name = "HeartRate";
                break;
            case 1:
                name = "Steps";
                break;
            case 2:
                name = "Posture";
                break;
            default:
                name = "unknown";
                break;
        }
        return name;
    }
}
