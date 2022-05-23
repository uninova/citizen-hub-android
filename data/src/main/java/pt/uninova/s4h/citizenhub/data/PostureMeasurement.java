package pt.uninova.s4h.citizenhub.data;

public class PostureMeasurement extends Measurement<PostureValue> {

    public PostureMeasurement(PostureValue value) {
        super(TYPE_POSTURE, value);
    }
}
