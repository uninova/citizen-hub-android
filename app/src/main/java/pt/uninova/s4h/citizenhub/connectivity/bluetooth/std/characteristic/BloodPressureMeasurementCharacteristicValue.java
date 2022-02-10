package pt.uninova.s4h.citizenhub.connectivity.bluetooth.std.characteristic;

import java.time.LocalDate;
import java.time.LocalTime;

public class BloodPressureMeasurementCharacteristicValue {

    public static final int UNIT_PRESSURE_MILLIMETRE_OF_MERCURY = 0;
    public static final int UNIT_PRESSURE_PASCAL = 1;

    private final byte flags;
    private final double systolicValue;
    private final double diastolicValue;
    private final double meanArterialPressureValue;
    private final LocalDate timeStampDate;
    private final LocalTime timeStampTime;
    private final double pulseRate;
    private final int userId;
    private final short measurementStatus;

    public BloodPressureMeasurementCharacteristicValue(byte[] buffer) {
        int offset = 0;

        flags = buffer[offset++];

        int multiplier = 1;

        if (getPressureUnit() == UNIT_PRESSURE_PASCAL) {
            multiplier = 1000;
        }

        systolicValue = Transcoding.sfloat(buffer[offset++], buffer[offset++]) * multiplier;
        diastolicValue = Transcoding.sfloat(buffer[offset++], buffer[offset++]) * multiplier;
        meanArterialPressureValue = Transcoding.sfloat(buffer[offset++], buffer[offset++]) * multiplier;

        if (hasTimeStamp()) {
            timeStampDate = Transcoding.date(buffer[offset++], buffer[offset++], buffer[offset++], buffer[offset++]);
            timeStampTime = Transcoding.time(buffer[offset++], buffer[offset++], buffer[offset++]);
        }

        if (hasPulseRate()) {
            pulseRate = Transcoding.sfloat(buffer[offset++], buffer[offset++]);
        }

        if (hasUserId()) {
            userId = Transcoding.uint8(buffer[offset++]);
        }

        if (hasMeasurementStatus()) {

        }
    }

    public int getPressureUnit() {
        return (flags & 0x1);
    }

    public boolean hasTimeStamp() {
        return (flags & 0x2) != 0;
    }

    public boolean hasPulseRate() {
        return (flags & 0x4) != 0;
    }

    public boolean hasUserId() {
        return (flags & 0x8) != 0;
    }

    public boolean hasMeasurementStatus() {
        return (flags & 0x10) != 0;
    }
}
