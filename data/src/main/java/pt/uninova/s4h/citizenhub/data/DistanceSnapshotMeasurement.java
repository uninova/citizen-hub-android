package pt.uninova.s4h.citizenhub.data;

public class DistanceSnapshotMeasurement extends SnapshotMeasurement<Double> {

    public DistanceSnapshotMeasurement(int type, double value) {
        super(TYPE_DISTANCE_SNAPSHOT, type, value);
    }
}
