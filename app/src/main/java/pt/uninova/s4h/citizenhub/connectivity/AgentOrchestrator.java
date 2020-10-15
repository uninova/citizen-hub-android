package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.persistence.*;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.UUIDv5;
import pt.uninova.util.messaging.Observer;

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
                    agentFactory.create(i, agent -> {
                        for (UUID j : agent.getPublicProtocolIds()) {
                            MeasuringProtocol p = (MeasuringProtocol) agent.getProtocol(j);

                            p.getMeasurementObservers().add(measurementRepository::add);
                        }

                        agent.enable();

                        deviceAgentMap.put(i, agent);
                    });
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
