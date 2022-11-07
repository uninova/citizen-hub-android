package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.BuildConfig;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.RoomSettingsManager;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class MiBand2Agent extends BluetoothAgent {

    public static final UUID XIAOMI_MIBAND2_SERVICE_AUTH = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    public static final UUID XIAOMI_MIBAND2_CHARACTERISTIC_AUTH = UUID.fromString("00000009-0000-3512-2118-0009af100700");

    public final static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2");

    static private final Set<Integer> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Measurement.TYPE_HEART_RATE,
            Measurement.TYPE_STEPS_SNAPSHOT
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            MiBand2HeartRateProtocol.ID,
            MiBand2StepsProtocol.ID
    )));

    private final Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>> bluetoothConnectionStateChange = value -> {
        if (value.getNewState() == BluetoothConnectionState.READY) {
            onStart();
        } else if (value.getNewState() == BluetoothConnectionState.DISCONNECTED) {
            onStop();
        }
    };

    private final Observer<StateChangedMessage<Integer, ? extends Protocol>> authenticationObserver = value -> {
        if (BuildConfig.DEBUG)
            System.out.println("MiBand2Agent.authenticationObserver " + value.getNewState() + " " + value.getOldState());

        if (value.getNewState() == Protocol.STATE_ENABLED) {
            setState(Agent.AGENT_STATE_ENABLED);

            final SetTimeProtocol setTimeProtocol = new SetTimeProtocol(getConnection(), this);
            setTimeProtocol.enable();
        }
    };

    private final MiBand2AuthenticationProtocol authenticationProtocol;

    public MiBand2Agent(BluetoothConnection connection, Context context) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection, new RoomSettingsManager(context, connection.getAddress()));
        authenticationProtocol = new MiBand2AuthenticationProtocol(connection, this);
    }

    @Override
    public void disable() {
        if (BuildConfig.DEBUG) {
            System.out.println("MiBand2Agent.disable");
        }

        getConnection().removeConnectionStateChangeListener(bluetoothConnectionStateChange);
        authenticationProtocol.removeStateObserver(authenticationObserver);
        super.disable();
    }

    private void onStart() {
        if (BuildConfig.DEBUG) {
            System.out.println("MiBand2Agent.onStart");
        }

        authenticationProtocol.enable();
    }

    private void onStop() {
        if (BuildConfig.DEBUG) {
            System.out.println("MiBand2Agent.onStop");
        }

        setState(Agent.AGENT_STATE_INACTIVE);
        getConnection().clear();
    }

    @Override
    public void enable() {
        if (BuildConfig.DEBUG) {
            System.out.println("MiBand2Agent.enable");
        }

        getConnection().addConnectionStateChangeListener(bluetoothConnectionStateChange);
        authenticationProtocol.addStateObserver(authenticationObserver);

        if (getConnection().getState() != BluetoothConnectionState.READY) {
            getConnection().connect();
        } else {
            onStart();
        }
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(int kind) {
        switch (kind) {
            case Measurement.TYPE_HEART_RATE:
                return new MiBand2HeartRateProtocol(this.getConnection(), getSampleDispatcher(), this);
            case Measurement.TYPE_STEPS_SNAPSHOT:
                return new MiBand2StepsProtocol(this.getConnection(), getSampleDispatcher(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "MI Band 2";
    }

    @Override
    public List<Fragment> getConfigurationFragments() {
        return null;
    }


    @Override
    public Fragment getPairingHelper() {
        return null;
    }
}