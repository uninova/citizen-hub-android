package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.AdjustReason;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.CurrentTime;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.core.ExactTime256;

public class SetTimeProtocol extends BluetoothProtocol {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.time");

    public SetTimeProtocol(BluetoothConnection connection, MiBand2Agent agent) {
        super(ID, connection, agent);
    }

    @Override
    public void enable() {
        setState(ProtocolState.ENABLING);

        getConnection().addCharacteristicListener(new BaseCharacteristicListener(BluetoothAgent.UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_1, BluetoothAgent.UUID_CHARACTERISTIC_CURRENT_TIME) {
            @Override
            public void onWrite(byte[] value) {
                setState(ProtocolState.COMPLETED);

                getConnection().removeCharacteristicListener(this);
            }
        });

        final CurrentTime currentTime = new CurrentTime(ExactTime256.of(LocalDateTime.now()), AdjustReason.of(false, false, false, false));
        final byte[] bytes = Arrays.copyOf(currentTime.toBytes(), 11);

        getConnection().writeCharacteristic(BluetoothAgent.UUID_MEMBER_ANHUI_HUAMI_INFORMATION_TECHNOLOGY_CO_LTD_1, BluetoothAgent.UUID_CHARACTERISTIC_CURRENT_TIME, bytes);
    }
}
