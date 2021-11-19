package pt.uninova.s4h.citizenhub.connectivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzPostureAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzRawProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2Agent;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSAgent;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnection;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnectionState;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.StateKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.messaging.Observer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

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

    public void create(String address, String agentType, Observer<Agent> observer) {
        if (agentType.equals(WearOSAgent.class.getSimpleName())) {
            wearOsFactory(address, observer);
        }
        else {
            bluetoothFactory(address, agentType, observer);
        }

    }

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

    public Agent createAgent(String agentType, BluetoothConnection connection) {
        Agent agent;

        Class<?> c = null;
        try {
            c = Class.forName(agentType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Constructor<?> cons = null;
        try {
            cons = c.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object object = null;
        try {
            object = cons.newInstance(connection);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        agent = ((Agent) object);
        return agent;
    }

    private void wearOsFactory(String address, Observer<Agent> observer) {
        connectionManager.put(ConnectionKind.WEAROS, address);
        WearOSConnection wearOSConnection = service.getWearOSMessageService().connect(address, service);
        wearOSConnection.addConnectionStateChangeListener(new Observer<StateChangedMessage<WearOSConnectionState, Agent>>() {
            @Override
            public void observe(StateChangedMessage<WearOSConnectionState, Agent> value) {
                if (value.getNewState() == WearOSConnectionState.READY) {
                    observer.observe(new WearOSAgent(wearOSConnection));
                    wearOSConnection.removeConnectionStateChangeListener(this);

                }
            }
        });
    }


    private void bluetoothFactory(String address, String agentName, Observer<Agent> observer) {
        final BluetoothConnection connection = new BluetoothConnection();
        Device device = deviceRepository.get(address);

        connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {
            @Override
            public void observe(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {

                if (value.getNewState() == BluetoothConnectionState.CONNECTED) {
                    device.setState(StateKind.INACTIVE);

                    connection.removeConnectionStateChangeListener(this);  //remove?
                }

                if (value.getNewState() == BluetoothConnectionState.DISCONNECTED) {
                    device.setState(StateKind.DISABLED); // if user choses to disconnect// it's just inactive.
                    connection.removeConnectionStateChangeListener(this);
                }

                if (value.getNewState() == BluetoothConnectionState.READY) {
                    device.setState(StateKind.ACTIVE);
                    connection.removeConnectionStateChangeListener(this);
                }
                observer.observe(createAgent(agentName, connection));


            }

        });

        device.setState(StateKind.INACTIVE);
        deviceRepository.update(device);

        try {
            bluetoothManager.getAdapter().getRemoteDevice(address).connectGatt(service, true, connection);

        } catch (Exception e) {
            e.printStackTrace();

        }
        deviceRepository.update(device);
    }

    private void bluetoothFactory(String address, Observer<Agent> observer) {
        if (BluetoothAdapter.checkBluetoothAddress(address))
            connectionManager.put(ConnectionKind.BLUETOOTH, address);
        final BluetoothConnection connection = new BluetoothConnection();

        connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {
            @Override
            public void observe(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {

                if (value.getNewState() == BluetoothConnectionState.READY) {
                    connection.removeConnectionStateChangeListener(this);

                    final String name = connection.getDevice().getName();
                    final String address = connection.getDevice().getAddress();

                    if (name.startsWith("HX")) { // && name.equals("HX-00043494")) {
                        observer.observe(new HexoSkinAgent(connection));
                    } else if (name.startsWith("MI")) {
                        observer.observe(new MiBand2Agent(connection));
                    } else if (connection.hasService(KbzRawProtocol.KBZ_SERVICE)) {
                        observer.observe(new KbzPostureAgent(connection));
                    } else if (name.startsWith("UprightGO2")) {
                        observer.observe(new UprightGo2Agent(connection));
                    }
                }
            }
        });

        bluetoothManager.getAdapter().getRemoteDevice(address).connectGatt(service, true, connection);
    }
}