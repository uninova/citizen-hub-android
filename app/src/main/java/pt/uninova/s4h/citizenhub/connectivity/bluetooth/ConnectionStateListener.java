package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

public interface ConnectionStateListener {
       UUID getDeviceAddress();

       UUID getServiceUuid();

       void onChange(byte[] value);
}
