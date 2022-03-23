package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class DayOfWeek implements ByteSerializable {

    public static final UInt8 UNKNOWN = UInt8.of(0);
    public static final UInt8 MONDAY = UInt8.of(1);
    public static final UInt8 TUESDAY = UInt8.of(2);
    public static final UInt8 WEDNESDAY = UInt8.of(3);
    public static final UInt8 THURSDAY = UInt8.of(4);
    public static final UInt8 FRIDAY = UInt8.of(5);
    public static final UInt8 SATURDAY = UInt8.of(6);
    public static final UInt8 SUNDAY = UInt8.of(7);

    private final UInt8 dayOfWeek;

    public DayOfWeek(UInt8 dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek(byte[] buffer) {
        this(new Buffer(buffer));
    }

    public DayOfWeek(Buffer reader) {
        this.dayOfWeek = reader.readUInt8();
    }

    public UInt8 getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public byte[] toBytes() {
        return this.dayOfWeek.toBytes();
    }

    @Override
    public void write(Buffer buffer) {
        dayOfWeek.write(buffer);
    }

}
