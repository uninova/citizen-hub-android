package pt.uninova.s4h.citizenhub.connectivity.wearos;

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
    public void onRead(byte[] value) {
    }

    @Override
    public void onReadFailure(byte[] value, int status) {
    }

    @Override
    public void onWrite(byte[] value) {
    }

    @Override
    public void onWriteFailure(byte[] value, int status) {
    }
}
