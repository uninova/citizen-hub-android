package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.os.Handler;
import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;


public class WearOSConnection /*extends AbstractProtocol */{
    private static final String TAG = "WearOSConnection";
    private final String address;
    private final Dispatcher<StateChangedMessage<WearOSConnectionState>> stateChangedMessageDispatcher;
    private final Map<String, Set<ChannelListener>> channelListenerMap;
    private WearOSConnectionState state;



    public WearOSConnection(String address) {

        this.address = address;
        state = WearOSConnectionState.DISCONNECTED;
        stateChangedMessageDispatcher = new Dispatcher<>();
        channelListenerMap = new ConcurrentHashMap<>();

    }
    public String getAddress(){
        return address;
    }

    public void addConnectionStateChangeListener(Observer<StateChangedMessage<WearOSConnectionState>> listener) {
        stateChangedMessageDispatcher.getObservers().add(listener);
    }
    public void removeConnectionStateChangeListener(Observer<StateChangedMessage<WearOSConnectionState>> listener) {
        stateChangedMessageDispatcher.getObservers().remove(listener);
     }


    public void addChannelListener(ChannelListener listener) {
        final String key = channelKey(listener);

        synchronized (channelListenerMap) {
            if (!channelListenerMap.containsKey(key)) {
                channelListenerMap.put(key, Collections.newSetFromMap(new ConcurrentHashMap<ChannelListener, Boolean>()));
            }
        }

        channelListenerMap.get(key).add(listener);
    }
    private String channelKey(String[] message) {
        String channelName = getMeasurementKind(message);
        return channelKey(channelName);
    }
    private String channelKey(ChannelListener listener) {
        return channelKey(listener.getChannelName());
    }
    private String channelKey(String name) {
        return name;
    }


    public void onCharacteristicChanged(String [] messageArray) {
        final String key = channelKey(messageArray);

        if (channelListenerMap.containsKey(key)) {
            for (ChannelListener i : channelListenerMap.get(key)) {
                i.onChange(getValue(messageArray),getTimeStamp(messageArray));
            }
        }
    }

    public double getValue(String[] message){
        double value = Double.parseDouble(message[1]);
        return value;
    }


    public Date getTimeStamp(String [] message){
        String timeStampString = message[2];
        Timestamp ts= Timestamp.valueOf(timeStampString);
        Date timestamp = new Date(ts.getTime());
        return timestamp;
    }

    private String getMeasurementKind(String [] message) {
        String measurementKind = message[3];
        return measurementKind;
    }

    public void close() {
        channelListenerMap.clear();
        stateChangedMessageDispatcher.close();
        

    }

    private void setState(WearOSConnectionState value) {
        if (value != state) {
            final WearOSConnectionState oldValue = state;

            this.state = value;

            stateChangedMessageDispatcher.dispatch(new StateChangedMessage<>(state, oldValue));
        }
    }

    public void disable() {
        setState(WearOSConnectionState.DISCONNECTED);
    }


    public void enable() {
        setState(WearOSConnectionState.READY);

    }

}
