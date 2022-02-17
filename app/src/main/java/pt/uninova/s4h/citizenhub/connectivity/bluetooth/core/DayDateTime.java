package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.time.LocalDateTime;

public class DayDateTime implements ByteSerializable {

    public static DayDateTime of(LocalDateTime localDateTime) {
        return new DayDateTime(DateTime.of(localDateTime), new DayOfWeek(UInt8.of(localDateTime.getDayOfWeek().getValue())));
    }

    private final DateTime dateTime;
    private final DayOfWeek dayOfWeek;

    public DayDateTime(DateTime dateTime, DayOfWeek dayOfWeek) {
        this.dateTime = dateTime;
        this.dayOfWeek = dayOfWeek;
    }

    public DayDateTime(Buffer reader) {
        this(new DateTime(reader), new DayOfWeek(reader));
    }

    public DayDateTime(byte[] buffer) {
        this(new Buffer(buffer));
    }

    @Override
    public byte[] toBytes() {
        final Buffer buffer = new Buffer(8);

        write(buffer);

        return buffer.getBytes();
    }

    @Override
    public void write(Buffer buffer) {
        dateTime.write(buffer);
        dayOfWeek.write(buffer);
    }
}
