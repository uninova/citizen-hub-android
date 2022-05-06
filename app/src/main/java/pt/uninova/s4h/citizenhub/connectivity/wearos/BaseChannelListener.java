package pt.uninova.s4h.citizenhub.connectivity.wearos;

import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.util.Date;

import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class BaseChannelListener implements ChannelListener {

    final private MeasurementKind name;

    public BaseChannelListener(MeasurementKind name) {
        this.name = name;

    }
    @Override
    public MeasurementKind getChannelName() {
        return name;
    }

    @Override
    public void onChange(double value, Date timestamp) {
    }

    @Override
    public void onWrite(byte[] value) {
    }

}
