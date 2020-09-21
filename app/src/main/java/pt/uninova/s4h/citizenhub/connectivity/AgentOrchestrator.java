package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzPostureProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2HeartRateProtocol;
import pt.uninova.s4h.citizenhub.persistence.*;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.UUIDv5;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class AgentOrchestrator {

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

    private final Map<Device, Agent> deviceAgentMap;

    public AgentOrchestrator(CitizenHubService service) {
        this.service = service;

        deviceRepository = new DeviceRepository(service.getApplication());
        featureRepository = new FeatureRepository(service.getApplication());
        measurementRepository = new MeasurementRepository(service.getApplication());
        agentFactory = new AgentFactory(service);

        deviceAgentMap = new HashMap<>();

        deviceRepository.getAll().observe(service, devices -> {
            final Set<Device> found = new HashSet<>(devices.size());

            for (Device i : devices) {
                found.add(i);

                if (!deviceAgentMap.containsKey(i)) {
                    // TODO: NOOOOOO
                    if (i.getName() != null) {
                        if (i.getName().equals("Mi Band 2")) {
                            agentFactory.create(i, agent -> {
                                MeasuringProtocol protocol = (MeasuringProtocol) agent.getProtocol(MiBand2HeartRateProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);

                                    protocol.enable();
                                }

                                protocol = (MeasuringProtocol) agent.getProtocol(MiBand2DistanceProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);

                                    protocol.enable();
                                }
                                deviceAgentMap.put(i, agent);
                            });
                        } else if (i.getName().equals("Posture Sensor")) {
                            agentFactory.create(i, agent -> {
                                MeasuringProtocol protocol = (MeasuringProtocol) agent.getProtocol(KbzPostureProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);

                                    protocol.enable();
                                }

                                protocol = (MeasuringProtocol) agent.getProtocol(MiBand2DistanceProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);

                                    protocol.enable();
                                }
                                deviceAgentMap.put(i, agent);
                            });

                        }
                    }
                }
            }

            trimAgents(found);
        });
    }

    public static UUIDv5 namespaceGenerator() {
        return NAMESPACE_GENERATOR;
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

}
