package pt.uninova.s4h.citizenhub.connectivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2HeartRateProtocol;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnection;
import pt.uninova.s4h.citizenhub.connectivity.wearos.WearOSConnectionState;
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

    public void destroy(ConnectionKind connectionKind, Observer<Agent> observer, Device device) {
        switch (connectionKind) {

            case UNKNOWN:
            case BLUETOOTH:
                bluetoothFactoryDestroy(device.getAddress(), observer);
                break;
            case WEAROS:
//                wearOsFactory(device.getAddress(), observer);
                break;
        }
    }

    private void bluetoothFactoryDestroy(String address, Observer<Agent> observer) {
        if (BluetoothAdapter.checkBluetoothAddress(address))
            unpairDevice(bluetoothManager.getAdapter().getRemoteDevice(address));
        //TODO we remove from the list but the unpair is not succesfull
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(ConnectionKind connectionKind, Observer<Agent> observer, Device device) {

        switch (connectionKind) {

            case UNKNOWN:
            case BLUETOOTH:
                bluetoothFactory(device.getAddress(), observer);
                break;
            case WEAROS:
                wearOsFactory(device.getAddress(), observer);
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
//                }
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
//                            AgentNotification agentNotification = new AgentNotification();
//                            agentNotification.agentAddEvent(new Device(name, address, ConnectionKind.BLUETOOTH.getId(), null));
//                        if ((connection.getServices().contains(HexoSkinHeartRateProtocol.UUID_SERVICE_HEART_RATE) &&
//                                connection.getServices().contains(HexoSkinRespirationProtocol.RESPIRATION_SERVICE_UUID) &&
//                                connection.getServices().contains(HexoSkinAccelerometerProtocol.ACCELEROMETER_SERVICE_UUID)) &&
                            if (name.startsWith("HX")) { // && name.equals("HX-00043494")) {
                                System.out.println("HEXOSKINNNNN");
                                observer.onChanged(new HexoSkinAgent(connection));
                            } else if ((connection.getServices().contains(MiBand2DistanceProtocol.UUID_SERVICE) &&
                                    connection.getServices().contains(MiBand2HeartRateProtocol.UUID_SERVICE_HEART_RATE)) && name.startsWith("Mi")) {
                                observer.onChanged(new MiBand2Agent(connection));
                            } else if (connection.hasService(KbzRawProtocol.KBZ_SERVICE)) {
                                observer.onChanged(new KbzPostureAgent(connection));
                            }
//                        try {


//                            deviceRepository.add(new Device(device.getName(), address, ConnectionKind.BLUETOOTH.getId(), null));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        }

                    }
                });
                System.out.println("antes connect");
//            BluetoothDevice device = bluetoothManager.getAdapter().getRemoteDevice(connection.getDevice().getAddress());
                bluetoothManager.getAdapter().getRemoteDevice(address).connectGatt(service, true, connection);
                System.out.println("depois connect");

            }

        }
    }
}


//    public void create(Device device, Observer<Agent> observer) {
//
//        if (BluetoothAdapter.checkBluetoothAddress(device.getAddress()))
//
//        {
//            final BluetoothConnection connection = new BluetoothConnection();
//
//            connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState>>() {
//                @Override
//                public void onChanged(StateChangedMessage<BluetoothConnectionState> value) {
//                    if (value.getNewState() == BluetoothConnectionState.READY) {
//                        connection.removeConnectionStateChangeListener(this);
//
//                        String name = connection.getDevice().getName();
//
//                        if (name != null && name.equals("HX-00043494")) {
//                            observer.onChanged(new HexoSkinAgent(connection));
//                        } else if (name != null && name.equals("MI Band 2")) {
//                            observer.onChanged(new MiBand2Agent(connection));
//                        } else if (connection.hasService(KbzRawProtocol.KBZ_SERVICE)) {
//                            observer.onChanged(new KbzPostureAgent(connection));
//                        }
//                    }
//                }
//            });
//
//            bluetoothManager.getAdapter().getRemoteDevice(device.getAddress()).connectGatt(service, true, connection);
//        }
//        else //TODO could be other than wearOS
//        {
//            WearOSConnection wearOSConnection = service.getWearOSMessageService().connect(device.getAddress(), service);
//            wearOSConnection.addConnectionStateChangeListener(new Observer<StateChangedMessage<WearOSConnectionState>>() {
//                @Override
//                public void onChanged(StateChangedMessage<WearOSConnectionState> value) {
//                    if (value.getNewState() == WearOSConnectionState.READY) {
//
//                        wearOSConnection.removeConnectionStateChangeListener(this);
//
//                        String name = device.getName();
//
//                        if (name != null) {
//                            observer.onChanged(new WearOSAgent(wearOSConnection));
//                        }
//                    }
//                }
//            });
//        }
//    }