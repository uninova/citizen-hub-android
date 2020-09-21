package pt.uninova.s4h.citizenhub.connectivity;

import android.bluetooth.BluetoothManager;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.messaging.Observer;

import static android.content.Context.BLUETOOTH_SERVICE;

public class AgentFactory {

    private final CitizenHubService service;
    private final BluetoothManager bluetoothManager;

    public AgentFactory(CitizenHubService service) {
        this.service = service;

        bluetoothManager = (BluetoothManager) service.getSystemService(BLUETOOTH_SERVICE);
    }

    public void create(Device device, Observer<Agent> observer) {
        if (true || device.getType().equals("BLUETOOTH")) { //TODO: duh
            final BluetoothConnection connection = new BluetoothConnection();

            connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState>>() {
                @Override
                public void onChanged(StateChangedMessage<BluetoothConnectionState> value) {
                    if (value.getNewState() == BluetoothConnectionState.READY) {
                        connection.removeConnectionStateChangeListener(this);
                        // TODO: identify device
                        observer.onChanged(new MiBand2Agent(connection));
                    }
                }
            });

            bluetoothManager.getAdapter().getRemoteDevice(device.getAddress()).connectGatt(service, true, connection);
            observer.onChanged(new MiBand2Agent(connection));
        }
    }
}
