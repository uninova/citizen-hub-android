package pt.uninova.s4h.citizenhub.connectivity.wearos;

import java.util.Date;

import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public interface ChannelListener {

    MeasurementKind getChannelName();

    void onChange(double value, Date timestamp);

    void onWrite(byte[] value);

}
