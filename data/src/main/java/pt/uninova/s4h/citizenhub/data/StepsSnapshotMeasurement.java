package pt.uninova.s4h.citizenhub.data;

public class StepsSnapshotMeasurement extends SnapshotMeasurement<Integer> {

    public StepsSnapshotMeasurement(int type, int value) {
        super(TYPE_STEPS_SNAPSHOT, type, value);
    }
}
