package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

import java.time.LocalDateTime;

public class ExactTime256 implements ByteSerializable {

    public static ExactTime256 of(LocalDateTime localDateTime) {
//        return new ExactTime256(DayDateTime.of(localDateTime), UInt8.of(localDateTime.getNano() / 3906250));
        return new ExactTime256(DayDateTime.of(localDateTime), UInt8.of(0));
    }

    private final DayDateTime dayDateTime;
    private final UInt8 fractions256;

    public ExactTime256(DayDateTime dayDateTime, UInt8 fractions256) {
        this.dayDateTime = dayDateTime;
        this.fractions256 = fractions256;
    }

    public ExactTime256(byte[] bytes) {
        this(new Buffer(bytes));
    }

    public ExactTime256(Buffer reader) {
        this.dayDateTime = new DayDateTime(reader);
        this.fractions256 = reader.readUInt8();
    }

    public DayDateTime getDayDateTime() {
        return dayDateTime;
    }

    public UInt8 getFractions256() {
        return fractions256;
    }

    @Override
    public byte[] toBytes() {
        final Buffer buffer = new Buffer(10);

        write(buffer);

        return buffer.getBytes();
    }

    @Override
    public void write(Buffer buffer) {
        dayDateTime.write(buffer);
        fractions256.write(buffer);
    }
}
