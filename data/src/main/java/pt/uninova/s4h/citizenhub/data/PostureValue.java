package pt.uninova.s4h.citizenhub.data;

public class PostureValue {

    public static final int CLASSIFICATION_UNKNOWN = 0;
    public static final int CLASSIFICATION_CORRECT = 1;
    public static final int CLASSIFICATION_INCORRECT = 2;

    private final int classification;
    private final double duration;

    public PostureValue(int classification, double duration) {
        this.classification = classification;
        this.duration = duration;
    }

    public int getClassification() {
        return classification;
    }

    public double getDuration() {
        return duration;
    }
}
