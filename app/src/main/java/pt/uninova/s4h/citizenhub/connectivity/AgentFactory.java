package pt.uninova.s4h.citizenhub.connectivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzPostureAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzRawProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo.UprightGoAgent;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSAgent;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnection;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnectionState;
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

        if (BluetoothAdapter.checkBluetoothAddress(device.getAddress()))

        {
            final BluetoothConnection connection = new BluetoothConnection();

            connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState>>() {
                @Override
                public void onChanged(StateChangedMessage<BluetoothConnectionState> value) {
                    if (value.getNewState() == BluetoothConnectionState.READY) {
                        connection.removeConnectionStateChangeListener(this);

                        String name = connection.getDevice().getName();

                        if (name != null && name.equals("HX-00043494")) {
                            observer.onChanged(new HexoSkinAgent(connection));
                        } else if (name != null && name.equals("MI Band 2")) {
                            observer.onChanged(new MiBand2Agent(connection));
                        } else if (connection.hasService(KbzRawProtocol.KBZ_SERVICE)) {
                            observer.onChanged(new KbzPostureAgent(connection));
                        } else if (name != null && name.equals("UprightGO2")){
                            observer.onChanged(new UprightGoAgent(connection));
                        }
                    }
                }
            });

            bluetoothManager.getAdapter().getRemoteDevice(device.getAddress()).connectGatt(service, true, connection);
        }
        else //TODO could be other than wearOS
        {
            WearOSConnection wearOSConnection = service.getWearOSMessageService().connect(device.getAddress(), service);
            wearOSConnection.addConnectionStateChangeListener(new Observer<StateChangedMessage<WearOSConnectionState>>() {
                @Override
                public void onChanged(StateChangedMessage<WearOSConnectionState> value) {
                    if (value.getNewState() == WearOSConnectionState.READY) {

                        wearOSConnection.removeConnectionStateChangeListener(this);

                        String name = device.getName();

                        if (name != null) {
                            observer.onChanged(new WearOSAgent(wearOSConnection));
                        }
                    }
                }
            });
        }
    }
}
