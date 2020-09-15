package pt.uninova.s4h.citizenhub.service;

import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.DeviceViewModel;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AgentFactory;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.GenericBluetoothAgent;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.util.UUIDv5;

public class DeviceManager {
    private static DeviceManager instance;
    private static UUIDv5 NAMESPACE_GENERATOR;

    static {
        try {
            NAMESPACE_GENERATOR = new UUIDv5("pt.uninova");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private final FeatureRepository featureRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceViewModel asd;
    private BluetoothManager bluetoothManager;
    private Service service;
    private Map<String, Device> deviceList;
    //TODO fazer lista de agentes, remover no disconnect, adicionar no connect etc
    private List<Agent> agentList;
    private AgentFactory agentFactory;
    private DeviceManager(CitizenHubService service) {
        this.service = service;
        deviceList = new HashMap<>();
        bluetoothManager = (BluetoothManager) service.getSystemService(Context.BLUETOOTH_SERVICE);
        deviceRepository = new DeviceRepository(service.getApplication());
        asd = new DeviceViewModel(service.getApplication());
        deviceRepository.getAll().observe(service, this::onGetAllDevicesChange);
        featureRepository = new FeatureRepository(service.getApplication());
        LiveData<List<Device>> deviceList = deviceRepository.getAll();
    }

    public static UUIDv5 namespaceGenerator() {
        return NAMESPACE_GENERATOR;
    }

    private void onGetAllDevicesChange(List<Device> deviceList) {
        for (Device device : deviceList
        ) {
            if (deviceRepository.get(device.getAddress()) != null) {
                // já se tinha conectado previamente
            }
            connect(device.getAddress());
        }
    }

    private LiveData<List<Device>> getAll() {
        for (Device device : Objects.requireNonNull(deviceRepository.getAll().getValue())) {
            deviceList.put(device.getAddress(), device);
        }
        return deviceRepository.getAll();
    }

    private List<Feature> getDeviceFeatures(String device_address) {
        return featureRepository.getAllSpecific(device_address);
    }

    private void ActiveFeature(String device_address, UUID feature_address) {
        // connection.enableNotifications();
    }

    private void DisableFeature(String device_address, String feature_address) {

    }

    public DeviceManager getDeviceManager(CitizenHubService service) {
        if (instance == null) {
            instance = new DeviceManager(service);
        }
        return instance;
    }

    public void connect(String address) {
        BluetoothConnection connection = new BluetoothConnection();
        bluetoothManager.getAdapter().getRemoteDevice(address).connectGatt(service, true, connection);
        Agent agent = attachConnection(connection);
    }

    public void disconnect(String address) {
        deviceList.remove(address);
    }

    public Agent attachConnection(BluetoothConnection connection) {
       agentFactory = new AgentFactory();
        Agent agent = agentFactory.createGenericAgent(connection);
        agentList.add(agent);
        return agent;
    }

}
