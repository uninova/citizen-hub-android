package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.bluetooth.BluetoothDevice;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;


public class WearOSConnection /*extends AbstractProtocol */ {
    private static final String TAG = "WearOSConnection";
    private final String address;
    private final Dispatcher<StateChangedMessage<WearOSConnectionState, WearOSConnection>> stateChangedMessageDispatcher;
    private final Map<MeasurementKind, Set<ChannelListener>> channelListenerMap;
    private WearOSConnectionState state;

    public WearOSConnection(String address) {
        this.address = address;

        state = WearOSConnectionState.DISCONNECTED;
        stateChangedMessageDispatcher = new Dispatcher<>();
        channelListenerMap = new ConcurrentHashMap<>();
    }

    public String getAddress() {
        return address;
    }

    public void addConnectionStateChangeListener(Observer<StateChangedMessage<WearOSConnectionState, WearOSConnection>> listener) {
        stateChangedMessageDispatcher.addObserver(listener);
    }

    public void removeConnectionStateChangeListener(Observer<StateChangedMessage<WearOSConnectionState, WearOSConnection>> listener) {
        stateChangedMessageDispatcher.removeObserver(listener);
    }


    public void addChannelListener(ChannelListener listener) {
        final MeasurementKind key = listener.getChannelName();

        synchronized (channelListenerMap) {
            if (!channelListenerMap.containsKey(key)) {
                channelListenerMap.put(key, Collections.newSetFromMap(new ConcurrentHashMap<ChannelListener, Boolean>()));
            }
        }

        channelListenerMap.get(key).add(listener);
    }

    public void onCharacteristicChanged(String[] messageArray) {
        final MeasurementKind key = MeasurementKind.find(Integer.parseInt(messageArray[2]));

        if (channelListenerMap.containsKey(key)) {
            for (ChannelListener i : channelListenerMap.get(key)) {
                i.onChange(getValue(messageArray), getTimeStamp(messageArray));
            }
        }
    }

    public double getValue(String[] message) {
        double value = Double.parseDouble(message[0]);
        return value;
    }


    public Date getTimeStamp(String[] message) {
        String timeStampString = message[1];
        return new Date(Long.parseLong(timeStampString));
    }

    public void close() {
        channelListenerMap.clear();
        stateChangedMessageDispatcher.close();


    }

    private void setState(WearOSConnectionState value) {
        System.out.println("GOT IN SETSTATE1");
        if (value != state) {
            final WearOSConnectionState oldValue = state;

            this.state = value;
            System.out.println("GOT IN SETSTATE2");
            stateChangedMessageDispatcher.dispatch(new StateChangedMessage<>(state, oldValue, this));
        }
    }

    public void disable() {
        setState(WearOSConnectionState.DISCONNECTED);
    }


    public void enable() {
        setState(WearOSConnectionState.READY);
    }
}
