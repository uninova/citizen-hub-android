package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractAgent;
import pt.uninova.s4h.citizenhub.connectivity.SettingsManager;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public abstract class BluetoothAgent extends AbstractAgent {

    public static final UUID UUID_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE_BLOOD_PRESSURE = UUID.fromString("00001810-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_CHARACTERISTIC_DATE_TIME = UUID.fromString("00002a08-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_CURRENT_TIME = UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_BLOOD_PRESSURE_MEASUREMENT = UUID.fromString("00002a35-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_DESCRIPTOR_CLIENT_CHARACTERISTIC_CONFIGURATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_1 = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_2 = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");

    final private BluetoothConnection connection;

    final private Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>> observer = (value) -> {
        if (value.getNewState() == BluetoothConnectionState.READY)
        {
            setState(AGENT_STATE_ENABLED);
            //TODO REMOVE AFTER TESTING
            System.out.println("Connection is Ready, agent set to ENABLED");
        }
        else {
            setState(AGENT_STATE_INACTIVE);
            //TODO REMOVE AFTER TESTING
            System.out.println("NOT READY, agent set to INACTIVE");

        }
    };

    protected BluetoothAgent(UUID id, Set<UUID> supportedProtocolsIds, Set<Integer> supportedMeasurements, BluetoothConnection connection, SettingsManager settingsManager) {
        super(id, connection.getSource(), supportedProtocolsIds, supportedMeasurements, settingsManager);

        this.connection = connection;
    }

    @Override
    public void disable() {
        connection.removeConnectionStateChangeListener(observer);
        connection.disconnect();

        this.setState(AGENT_STATE_DISABLED);
    }

    @Override
    public void enable() {
        this.connection.addConnectionStateChangeListener(observer);

        if (connection.getState() == BluetoothConnectionState.READY) {
            this.setState(AGENT_STATE_ENABLED);
        } else {
            this.setState(AGENT_STATE_INACTIVE);
            this.connection.connect();
        }
    }

    public BluetoothConnection getConnection() {
        return connection;
    }
}
