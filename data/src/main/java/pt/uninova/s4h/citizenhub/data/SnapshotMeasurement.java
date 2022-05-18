package pt.uninova.s4h.citizenhub.data;

public abstract class SnapshotMeasurement<T> extends Measurement<T> {

    public static final int TYPE_DAY = 0;

    private final int snapshotType;

    protected SnapshotMeasurement(int measurementType, int snapshotType, T value) {
        super(measurementType, value);

        this.snapshotType = snapshotType;
    }

    public int getSnapshotType() {
        return snapshotType;
    }
}
