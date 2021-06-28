package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.AgentState;
import pt.uninova.s4h.citizenhub.connectivity.Protocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public class KbzPostureAgent extends BluetoothAgent {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture");

    public KbzPostureAgent(BluetoothConnection connection) {
        super(ID, createProtocols(connection), connection);
    }

    private static Map<UUID, Protocol> createProtocols(BluetoothConnection connection) {
        final Map<UUID, Protocol> protocolMap = new HashMap<>();

        protocolMap.put(KbzRawProtocol.ID, new KbzRawProtocol(connection));

        return protocolMap;
    }

    @Override
    public void disable() {
        for (UUID i : getPublicProtocolIds(ProtocolState.ENABLED)) {
            getProtocol(i).disable();
        }

        getConnection().close();

        setState(AgentState.DISABLED);
    }

    @Override
    public void enable() {
        KbzRawProtocol protocol = (KbzRawProtocol) getProtocol(KbzRawProtocol.ID);

        protocol.getObservers().add(new Observer<StateChangedMessage<ProtocolState>>() {
            @Override
            public void onChanged(StateChangedMessage<ProtocolState> value) {
                if (value.getNewState() == ProtocolState.ENABLED) {
                    KbzPostureAgent.this.setState(AgentState.ENABLED);

                    protocol.getObservers().remove(this);
                }
            }
        });

        protocol.enable();
    }

    @Override
    public List<MeasurementKind> getSupportedMeasurements() {
        List<MeasurementKind> measurementKindList = new ArrayList<>();
        measurementKindList.add(MeasurementKind.SITTING);
        measurementKindList.add(MeasurementKind.STANDING);
        measurementKindList.add(MeasurementKind.GOOD_POSTURE);
        measurementKindList.add(MeasurementKind.BAD_POSTURE);

        return measurementKindList;
    }

    @Override
    public void enableMeasurement(MeasurementKind measurementKind) {
        switch (measurementKind) {

            case GOOD_POSTURE:
            case BAD_POSTURE:
            case SITTING:
            case STANDING:
                getProtocol(KbzRawProtocol.ID).enable();
            case UNKNOWN:
                break;
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
