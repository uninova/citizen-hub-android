package pt.uninova.s4h.citizenhub.connectivity.wearos;

import java.sql.Timestamp;
import java.util.Date;

public class BaseChannelListener implements ChannelListener {

    final private String name;


    public BaseChannelListener(String name) {
        this.name = name;

    }
    @Override
    public String getChannelName() {
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
