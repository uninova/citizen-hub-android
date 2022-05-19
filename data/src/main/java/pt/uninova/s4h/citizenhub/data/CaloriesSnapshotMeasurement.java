package pt.uninova.s4h.citizenhub.data;

public class CaloriesSnapshotMeasurement extends SnapshotMeasurement<Double> {

    public CaloriesSnapshotMeasurement(int type, double value) {
        super(TYPE_CALORIES_SNAPSHOT, type, value);
    }
}
