package pt.uninova.s4h.citizenhub.connectivity.wearos;

import java.util.Date;

public interface ChannelListener {

    int getChannelName();

    void onChange(double value, Date timestamp);

    void onWrite(byte[] value);

}
