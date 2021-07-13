package pt.uninova.s4h.citizenhub.connectivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzPostureAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzRawProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UpRightGo2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UpRightGo2Protocol;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnection;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnectionState;
import pt.uninova.s4h.citizenhub.persistence.AgentStateAnnotation;
import pt.uninova.s4h.citizenhub.persistence.AgentStateAnnotation.StateAnnotation;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.Device;
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

    public HashSet<String> getAgentList() {
        HashSet<String> agentList = new HashSet<>();
        agentList.add(HexoSkinAgent.class.getSimpleName());
        agentList.add(MiBand2Agent.class.getSimpleName());
        agentList.add(KbzPostureAgent.class.getSimpleName());
        return agentList;
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

                }
            }
        });
    }

    private Agent create(String address, String agentName, Observer<Agent> observer) {
        Agent agent = null;
        final BluetoothConnection connection = new BluetoothConnection();

        List<Device> deviceList = deviceRepository.getWithAgent(agentName);
        for (Device device : deviceList
        ) {
            if (getAgentList().contains(device.getAgentType())) {
                switch (device.getAgentType()) {
                    case "HexoSkinAgent":

                        agent = new HexoSkinAgent();
                        break;
                    case "KbzPostureAgent":
                        agent = new KbzPostureAgent();
                        break;
                    case "MiBand2Agent":
                        agent = new MiBand2Agent();
                        break;
                    case "UpRightGo2Agent":
                        agent = new UpRightGo2Agent();
                        break;
                    default:
                        return null;
                }
                device.setState(StateAnnotation.class.cast(AgentStateAnnotation.INACTIVE));
                deviceRepository.update(device);

                try {
                    bluetoothManager.getAdapter().getRemoteDevice(address).connectGatt(service, true, connection);
                    //TODO onReady passar a active
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return agent;
    }

    private void bluetoothFactory(String address, Observer<Agent> observer) {
        if (BluetoothAdapter.checkBluetoothAddress(address))
            connectionManager.put(ConnectionKind.BLUETOOTH, address);
        {
            {
                deviceRepository.get(address).setState(StateAnnotation.class.cast(AgentStateAnnotation.INACTIVE));
                final BluetoothConnection connection = new BluetoothConnection();

                connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState>>() {
                    @Override
                    public void onChanged(StateChangedMessage<BluetoothConnectionState> value) {

                        if (value.getNewState() == BluetoothConnectionState.READY) {
                            connection.removeConnectionStateChangeListener(this);

                            String name = connection.getDevice().getName();
                            System.out.println("ONCHANGED NAME" + "" + name);

//                        if ((connection.getServices().contains(HexoSkinHeartRateProtocol.UUID_SERVICE_HEART_RATE) &&
//                                connection.getServices().contains(HexoSkinRespirationProtocol.RESPIRATION_SERVICE_UUID) &&
//                                connection.getServices().contains(HexoSkinAccelerometerProtocol.ACCELEROMETER_SERVICE_UUID)) &&
                            if (name.startsWith("HX")) { // && name.equals("HX-00043494")) {
                                deviceRepository.get(address).setAgentType(HexoSkinAgent.class.getSimpleName());
                                observer.onChanged(new HexoSkinAgent(connection));
                            } else if (/*(connection.getServices().contains(MiBand2DistanceProtocol.UUID_SERVICE) &&
                                    connection.getServices().contains(MiBand2HeartRateProtocol.UUID_SERVICE_HEART_RATE)) &&*/ name.startsWith("MI")) {
                                deviceRepository.get(address).setAgentType(MiBand2Agent.class.getSimpleName());
                                observer.onChanged(new MiBand2Agent(connection));
                            } else if (connection.hasService(KbzRawProtocol.KBZ_SERVICE)) {
                                deviceRepository.get(address).setAgentType(KbzPostureAgent.class.getSimpleName());
                                observer.onChanged(new KbzPostureAgent(connection));
                            } else if (name.startsWith("UprightGO2")) {
                                deviceRepository.get(address).setAgentType(UpRightGo2Protocol.class.getSimpleName());
                                observer.onChanged(new UpRightGo2Agent(connection));
                            }
                            deviceRepository.get(address).setState(StateAnnotation.class.cast(AgentStateAnnotation.ACTIVE));
                            deviceRepository.update(deviceRepository.get(address));
                        }
                    }
                });
                bluetoothManager.getAdapter().getRemoteDevice(address).connectGatt(service, true, connection);
            }

        }
    }
}