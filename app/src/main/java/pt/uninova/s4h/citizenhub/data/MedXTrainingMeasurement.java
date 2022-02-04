package pt.uninova.s4h.citizenhub.data;

public class MedXTrainingMeasurement extends Measurement<MedXTrainingValue> {

    public MedXTrainingMeasurement(MedXTrainingValue value) {
        super(Measurement.LUMBAR_EXTENSION_TRAINING, value);
    }
}
