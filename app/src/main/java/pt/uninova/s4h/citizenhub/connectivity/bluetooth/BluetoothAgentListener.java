package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

public interface BluetoothAgentListener {

    void onStateChange(BluetoothAgent agent, int state);

}
