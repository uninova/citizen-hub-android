package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.TypeConverter;

public class CharacteristicTypeConverter {

    public enum CharacteristicType {
        Unknown(0),
        Heartrate(1),
        Steps(2),
        Distance(3),
        Calories(4),
        GoodPosture(5),
        BadPosture(6);


        private final int numeral;

        CharacteristicType(int value) {
            this.numeral = value;
        }

        public int getCode() {
            return numeral;
        }

        @TypeConverter
        public static CharacteristicType getCharacteristicName(int numeral) {
            for (CharacteristicType characteristicType : values()) {
                if (characteristicType.numeral == numeral) {
                    return characteristicType;
                }
            }
            return null;
        }

        @TypeConverter
        public static int getCharacteristicTypeInt(CharacteristicType type) {
            return type.numeral;
        }
    }
}





