package pt.uninova.s4h.citizenhub.connectivity.wearos;

import java.sql.Timestamp;
import java.util.Date;

public interface ChannelListener {

    String getChannelName();

    void onChange(double value, Date timestamp);

    void onRead(byte[] value);

    void onReadFailure(byte[] value, int status);

    void onWrite(byte[] value);

    void onWriteFailure(byte[] value, int status);

}
