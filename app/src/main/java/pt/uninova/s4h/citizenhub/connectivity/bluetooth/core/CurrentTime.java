package pt.uninova.s4h.citizenhub.connectivity.bluetooth.core;

public class CurrentTime implements ByteSerializable {

    private final ExactTime256 exactTime256;
    private final AdjustReason adjustReason;

    public CurrentTime(ExactTime256 exactTime256, AdjustReason adjustReason) {
        this.exactTime256 = exactTime256;
        this.adjustReason = adjustReason;
    }

    public CurrentTime(Buffer buffer) {
        this(new ExactTime256(buffer), new AdjustReason(buffer));
    }

    public CurrentTime(byte[] bytes) {
        this(new Buffer(bytes));
    }

    @Override
    public byte[] toBytes() {
        final Buffer buffer = new Buffer(10);

        write(buffer);

        return buffer.getBytes();
    }

    @Override
    public void write(Buffer buffer) {
        exactTime256.write(buffer);
        adjustReason.write(buffer);
    }
}
