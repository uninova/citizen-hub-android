package pt.uninova.s4h.citizenhub.connectivity.bluetooth.std.characteristic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Transcoding {

    public static LocalDate date(byte b1, byte b2, byte b3, byte b4) {
        final int year = uint16(b1, b2);
        final int month = uint8(b3);
        final int day = uint8(b4);

        if (year == 0 || month == 0 || day == 0) {
            return null;
        }

        return LocalDate.of(year, month, day);
    }

    public static double sfloat(byte b1, byte b2) {
        final int exponent = (b1 & 0xf0) >>> 4;
        final int mantissa = (b1 & 0x0f) << 8 | b2 & 0xff;

        return mantissa * Math.pow(10, exponent);
    }

    public static LocalTime time(byte b1, byte b2, byte b3) {
        return LocalTime.of(uint8(b1), uint8(b2), uint8(b3));
    }

    public static int uint8(byte b1) {
        return b1 & 0xff;
    }

    public static int uint16(byte b1, byte b2) {
        return (b1 & 0xff) << 8 | b2 & 0xff;
    }

}
