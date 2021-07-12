package pt.uninova.s4h.citizenhub.connectivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzPostureAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzRawProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UpRightGo2Agent;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnection;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnectionState;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.messaging.Observer;

import static android.content.Context.BLUETOOTH_SERVICE;

public class AgentFactory {

    private final CitizenHubService service;
    private final BluetoothManager bluetoothManager;
    private final HashMap<ConnectionKind, String> connectionManager = new LinkedHashMap<>();
    private final DeviceRepository deviceRepository;

    public AgentFactory(CitizenHubService service) {
        this.service = service;
        bluetoothManager = (BluetoothManager) service.getSystemService(BLUETOOTH_SERVICE);
        deviceRepository = new DeviceRepository(service.getApplication());
    }

    public void destroy(ConnectionKind connectionKind, String address, Observer<Agent> observer) {
        switch (connectionKind) {

            case UNKNOWN:
            case BLUETOOTH:
                bluetoothFactoryDestroy(address, observer);
                break;
            case WEAROS:
//                wearOsFactory(address, observer);
                break;
        }
    }

    private void bluetoothFactoryDestroy(String address, Observer<Agent> observer) {
        if (BluetoothAdapter.checkBluetoothAddress(address))
            unpairDevice(address);
    }

    private void unpairDevice(String address) {
        try {
            Method method = bluetoothManager.getAdapter().getRemoteDevice(address).getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(bluetoothManager.getAdapter().getRemoteDevice(address), (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //deixar so 1 para cada protocolo e dar nome aos protocolo;
    // testar testar testar
    // passar os observers sempre para o fim
    //roda dentada
    //usar so addresses
    //TODO distinguir os estados dos devices desired state -> Active, antes de estar ou por ter corrido mal ->inactive, desligado = disabled
    //TODO criar create que sabemos o que é com agentType +


    public void create(ConnectionKind connectionKind, String address, Observer<Agent> observer) {

        switch (connectionKind) {

            case UNKNOWN:
            case BLUETOOTH:
                bluetoothFactory(address, observer);
                break;
            case WEAROS:
                wearOsFactory(address, observer);
                break;
        }

    }

    private void wearOsFactory(String address, Observer<Agent> observer) {
        connectionManager.put(ConnectionKind.WEAROS, address);
        WearOSConnection wearOSConnection = service.getWearOSMessageService().connect(address, service);
        wearOSConnection.addConnectionStateChangeListener(new Observer<StateChangedMessage<WearOSConnectionState>>() {
            @Override
            public void onChanged(StateChangedMessage<WearOSConnectionState> value) {
                if (value.getNewState() == WearOSConnectionState.READY) {

                    wearOSConnection.removeConnectionStateChangeListener(this);

//                    String name = device.getName();

//                    if (name != null) {
//                    try {
//                        observer.onChanged(new WearOSAgent(wearOSConnection));
//                        deviceRepository.add(new Device("wearOS", wearOSConnection.getAddress(), ConnectionKind.WEAROS.getId(), null));
//                    } catch (Exception e) {
//                        e.printStackTrace();
                }
            }
        });
    }

    private void bluetoothFactory(String address, Observer<Agent> observer) {
        if (BluetoothAdapter.checkBluetoothAddress(address))
            connectionManager.put(ConnectionKind.BLUETOOTH, address);
        {
            {
                final BluetoothConnection connection = new BluetoothConnection();

                connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState>>() {
                    @Override
                    public void onChanged(StateChangedMessage<BluetoothConnectionState> value) {


                        System.out.println("ONCHANGED" + " " + value.getNewState());

                        if (value.getNewState() == BluetoothConnectionState.READY) {
                            connection.removeConnectionStateChangeListener(this);

                            String name = connection.getDevice().getName();
                            System.out.println("ONCHANGED NAME" + "" + name);

//                        if ((connection.getServices().contains(HexoSkinHeartRateProtocol.UUID_SERVICE_HEART_RATE) &&
//                                connection.getServices().contains(HexoSkinRespirationProtocol.RESPIRATION_SERVICE_UUID) &&
//                                connection.getServices().contains(HexoSkinAccelerometerProtocol.ACCELEROMETER_SERVICE_UUID)) &&
                            if (name.startsWith("HX")) { // && name.equals("HX-00043494")) {
                                System.out.println("HEXOSKINNNNN");
                                observer.onChanged(new HexoSkinAgent(connection));
                            } else if (/*(connection.getServices().contains(MiBand2DistanceProtocol.UUID_SERVICE) &&
                                    connection.getServices().contains(MiBand2HeartRateProtocol.UUID_SERVICE_HEART_RATE)) &&*/ name.startsWith("MI")) {
                                observer.onChanged(new MiBand2Agent(connection));
                            } else if (connection.hasService(KbzRawProtocol.KBZ_SERVICE)) {
                                observer.onChanged(new KbzPostureAgent(connection));
                            } else if (name.startsWith("UprightGO2")){
                                observer.onChanged(new UpRightGo2Agent(connection));
                            }
                        }
                    }
                });
                System.out.println("antes connect");
                bluetoothManager.getAdapter().getRemoteDevice(address).connectGatt(service, true, connection);
                System.out.println("depois connect");

            }

        }
    }
}