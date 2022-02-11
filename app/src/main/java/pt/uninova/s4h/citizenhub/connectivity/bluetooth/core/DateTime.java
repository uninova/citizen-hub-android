package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.time.LocalDateTime;

public class DateTime implements Bufferable, Byteable {

    private final UInt16 year;
    private final UInt8 month;
    private final UInt8 day;
    private final UInt8 hours;
    private final UInt8 minutes;
    private final UInt8 seconds;

    public DateTime(LocalDateTime localDateTime) {
        this(UInt16.of(localDateTime.getYear()), UInt8.of(localDateTime.getMonthValue()), UInt8.of(localDateTime.getDayOfMonth()), UInt8.of(localDateTime.getHour()), UInt8.of(localDateTime.getMinute()), UInt8.of(localDateTime.getSecond()));
    }

    public DateTime(UInt16 year, UInt8 month, UInt8 day, UInt8 hours, UInt8 minutes, UInt8 seconds) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public DateTime(byte[] buffer) {
        this(buffer, 0);
    }

    public DateTime(byte[] buffer, int offset) {
        this(new ByteReader(buffer, offset));
    }

    public DateTime(ByteReader reader) {
        year = reader.readUInt16();
        month = reader.readUInt8();
        day = reader.readUInt8();
        hours = reader.readUInt8();
        minutes = reader.readUInt8();
        seconds = reader.readUInt8();
    }

    @Override
    public void buffer(ByteWriter writer) {
        year.buffer(writer);
        month.buffer(writer);
        day.buffer(writer);
        hours.buffer(writer);
        minutes.buffer(writer);
        seconds.buffer(writer);
    }

    public UInt8 getDay() {
        return day;
    }

    public UInt8 getHours() {
        return hours;
    }

    public UInt8 getMinutes() {
        return minutes;
    }

    public UInt8 getMonth() {
        return month;
    }

    public UInt8 getSeconds() {
        return seconds;
    }

    public UInt16 getYear() {
        return year;
    }

    public boolean isValidDate() {
        return year.toInt() > 1532 && year.toInt() < 9999 && month.toInt() > 0 && month.toInt() < 13 && day.toInt() > 0 && day.toInt() < 31;
    }

    @Override
    public byte[] toBytes() {
        final byte[] buffer = new byte[7];

        buffer(new ByteWriter(buffer));

        return buffer;
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hours.toInt(), minutes.toInt(), seconds.toInt());
    }

}
