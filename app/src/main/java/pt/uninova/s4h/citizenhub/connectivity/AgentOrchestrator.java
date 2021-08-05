package pt.uninova.s4h.citizenhub.connectivity;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.AgentListChangeMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.Feature;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;
import pt.uninova.s4h.citizenhub.persistence.StateKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.UUIDv5;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public class
AgentOrchestrator {

    private static final String TAG = "AgentOrchestrator";
    private static UUIDv5 NAMESPACE_GENERATOR;

    static {
        try {
            NAMESPACE_GENERATOR = new UUIDv5("pt.uninova");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private final CitizenHubService service;
    private final DeviceRepository deviceRepository;
    private final FeatureRepository featureRepository;
    private final MeasurementRepository measurementRepository;
    private final AgentFactory agentFactory;
    private final static Observer<StateChangedMessage<AgentState, Class<?>>> observer = value -> {

    };
    private final Map<Device, Agent> deviceAgentMap = new HashMap<>();
    private final Dispatcher<AgentListChangeMessage> eventMessageDispatcher;
    private List<Device> devices;

    public AgentOrchestrator(CitizenHubService service) {
        this.service = service;
        devices = new ArrayList<>();
        deviceRepository = new DeviceRepository(service.getApplication());
        featureRepository = new FeatureRepository(service.getApplication());
        measurementRepository = new MeasurementRepository(service.getApplication());
        agentFactory = new AgentFactory(service);
        eventMessageDispatcher = new Dispatcher<>();

//        ((CitizenHubServiceBound) getService().getAgentOrchestrator().addAgentEventListener(value -> {
//            deviceList.clear();
//            for (Device device : value.getDeviceList()) {
//                deviceList.add(new DeviceListItem(device));
//            }
//
//            activity.runOnUiThread(() -> adapter.notifyDataSetChanged());
//
//        });
        deviceRepository.obtainAll(value -> {
            for (Device i : value
            ) {
                if (i.getAgentType() != null) {
                    agentFactory.create(i.getAddress(), i.getAgentType(), agent -> {
                        agent.enable();
                        agent.getObservers().add(observer);
                        deviceAgentMap.put(i, agent);
                        devices.add(i);
                        featureRepository.obtainKindsFromDevice(i.getAddress(), measurementKinds -> {
                            for (UUID j : agent.getPublicProtocolIds()) {
                                ((MeasuringProtocol) agent.getProtocol(j)).getMeasurementObservers().add(measurementRepository::add);
                            }
                            for (MeasurementKind measurementKind : measurementKinds
                            ) {
                                if (getDeviceAgentMap().get(i).getSupportedMeasurements().contains(measurementKind)) {
                                    getDeviceAgentMap().get(i).enableMeasurement(measurementKind);
                                }
                            }
                        });
                    });

                }
                agentFactory.create(i.getConnectionKind(), i.getAddress(), agent -> {
                    agent.enable();
                    i.setConnectionKind(ConnectionKind.BLUETOOTH);
                    i.setState(StateKind.INACTIVE);
                    i.setAgentType(agent.getName());
                    deviceAgentMap.put(i, agent);
                    devices.add(i);
                    featureRepository.obtainKindsFromDevice(i.getAddress(), measurementKinds -> {
                        for (UUID j : agent.getPublicProtocolIds()) {
                            ((MeasuringProtocol) agent.getProtocol(j)).getMeasurementObservers().add(measurementRepository::add);
                        }
                        for (MeasurementKind measurementKind : measurementKinds
                        ) {
                            if (getDeviceAgentMap().get(i).getSupportedMeasurements().contains(measurementKind)) {
                                getDeviceAgentMap().get(i).enableMeasurement(measurementKind);
                            }
                        }
                    });
                });
            }
            System.out.println("After create" + deviceAgentMap.size());
        });
    }

    public static UUIDv5 namespaceGenerator() {
        return NAMESPACE_GENERATOR;
    }

    public List<MeasurementKind> getSupportedFeatures(String device_name) {
        if (device_name != null && device_name.equals("HX-00043494")) {
            return new HexoSkinAgent().getSupportedMeasurements();
        } else if (device_name != null && device_name.equals("MI Band 2")) {
            return new MiBand2Agent().getSupportedMeasurements();

        }
        return null;
    }

    public void addAgentEventListener(Observer<AgentListChangeMessage> listener) {
        eventMessageDispatcher.getObservers().add(listener);
    }

    public void add(Device device, Agent agent) {
        deviceAgentMap.put(device, agent);
    }

    public void addDevice(Device device) {
        deviceAgentMap.put(device, null);
        devices = getDevicesFromMap();
        eventMessageDispatcher.dispatch(new AgentListChangeMessage(devices));
        agentFactory.create(device.getConnectionKind(), device.getAddress(), agent -> {
            agent.enable();
            deviceAgentMap.put(device, agent);
            devices = getDevicesFromMap();

        });

    }


    public void deleteDeviceFromMap(Device device) {
        Agent agent = deviceAgentMap.get(device);
        deviceAgentMap.remove(device);
        agent.disable();
        agent.getObservers().clear();
        //TODO fazer close
        devices = getDevicesFromMap();
        eventMessageDispatcher.dispatch(new AgentListChangeMessage(devices));
    }

    public List<MeasurementKind> getSupportedFeatures(Device device) {
        return getSupportedFeatures(device.getName());
    }

    public List<Feature> getEnabledFeatures(Device device) {
        return getEnabledFeatures(device);
    }

    public Map<Device, Agent> getDeviceAgentMap() {
        return deviceAgentMap;
    }

    public List<Device> getDevicesFromMap() {
        List<Device> deviceMap = new ArrayList<>();
        deviceAgentMap.forEach((device, agent) -> deviceMap.add(device));
        return deviceMap;
    }

    private void trimAgents(Set<Device> devices) {
        for (Device i : deviceAgentMap.keySet()) {
            if (!devices.contains(i)) {
                final Agent agent = deviceAgentMap.get(i);

                if (agent != null) {
                    agent.disable();
                }

                deviceAgentMap.remove(i);
            }
        }
    }

    public void close() {
        for (Device i : deviceAgentMap.keySet()) {
            deviceAgentMap.get(i).disable();
        }
    }


    public void enableMeasurement(Device value, MeasurementKind measurementKind) {
        Objects.requireNonNull(getDeviceAgentMap().get(value)).enableMeasurement(measurementKind);
    }
}
