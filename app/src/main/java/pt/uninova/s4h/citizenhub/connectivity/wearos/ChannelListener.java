package pt.uninova.s4h.citizenhub.connectivity.wearos;

import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.Date;

public interface ChannelListener {

    MeasurementKind getChannelName();

    void onChange(double value, Date timestamp);

    void onWrite(byte[] value);

}
