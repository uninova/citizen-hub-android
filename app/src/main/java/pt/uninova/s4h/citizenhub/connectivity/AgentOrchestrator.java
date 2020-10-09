package pt.uninova.s4h.citizenhub.connectivity;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAccelerometerProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinHeartRateProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinRespirationProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzPostureProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2DistanceProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2HeartRateProtocol;
import pt.uninova.s4h.citizenhub.persistence.Device;
import pt.uninova.s4h.citizenhub.persistence.DeviceRepository;
import pt.uninova.s4h.citizenhub.persistence.FeatureRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.UUIDv5;

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
                        if (i.getName().equals("MI Band 2")) {
                            agentFactory.create(i, agent -> {
                                MeasuringProtocol protocol = (MeasuringProtocol) agent.getProtocol(MiBand2HeartRateProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);
                                }

                                protocol = (MeasuringProtocol) agent.getProtocol(MiBand2DistanceProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);
                                }

                                agent.enable();

                                deviceAgentMap.put(i, agent);
                            });
                        } else if (i.getName().equals("HX-00043494")) {
                            agentFactory.create(i, agent -> {
                                MeasuringProtocol protocol = (MeasuringProtocol) agent.getProtocol(HexoSkinHeartRateProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);

                                    protocol.enable();
                                }

                                protocol = (MeasuringProtocol) agent.getProtocol(HexoSkinRespirationProtocol.ID);

                                if (protocol != null) {
                                    protocol.getMeasurementObservers().add(measurementRepository::add);

                                    protocol.enable();
                                }

                                protocol = (MeasuringProtocol) agent.getProtocol(HexoSkinAccelerometerProtocol.ID);

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
/* // Respiration Rate Service UUID
    private static UUID RESPIRATION_SERVICE_UUID = UUID.fromString("3b55c581-bc19-48f0-bd8c-b522796f8e24");

    // Respiration Rate Measurement UUID
    private static UUID RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("9bc730c3-8cc0-4d87-85bc-573d6304403c");

    // UUID for notification
    private static UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // Accelerometer Service UUID
    private static UUID ACCELEROMETER_SERVICE_UUID = UUID.fromString("bdc750c7-2649-4fa8-abe8-fbf25038cda3");

    // Accelerometer Measurement UUID
    private static UUID ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("75246a26-237a-4863-aca6-09b639344f43");

 // Respiration Rate Received
                else if (uuid.equals(RESPIRATION_RATE_MEASUREMENT_CHARACTERISTIC_UUID)) {

                    byte flag = data[0];
                    int format;
                    if ((flag & 0x01) == 0) {
                        format = BluetoothGattCharacteristic.FORMAT_UINT8;
                    } else {
                        format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    }

                    int respRate = characteristic.getIntValue(format, 1);
                    _data.set(2, "RESP. RATE " + respRate + ", (" + hexString + ")");

                    boolean isInspExpPresent = (flag & 0x02) != 0;
                    _data.set(3, "INSP/EXP ");
                    if (isInspExpPresent) {
                        int startOffset = 1 + (format == BluetoothGattCharacteristic.FORMAT_UINT8 ? 1 : 2);
                        boolean inspFirst = (flag & 0x04) == 0;
                        StringBuilder sb = new StringBuilder();
                        sb.append("INSP/EXP ");
                        for (int i = startOffset; i < data.length; i += 2) {
                            float value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, i) / 32.0f;
                            if (inspFirst) {
                                sb.append(value).append("(I), ");
                                inspFirst = false;
                            } else {
                                sb.append(value).append("(E), ");
                                inspFirst = true;
                            }
                        }
                        _data.set(3, sb.toString());
                    }
                    _adapter.notifyDataSetChanged();
                }

                // Accelerometer data received
                else if (uuid.equals((ACCELEROMETER_MEASUREMENT_CHARACTERISTIC_UUID))) {
                    byte flag = data[0];
                    int format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    int dataIndex = 1;

                    boolean isStepCountPresent = (flag & 0x01) != 0;
                    boolean isActivityPresent = (flag & 0x02) != 0;
                    boolean isCadencePresent = (flag & 0x04) != 0;

                    if (isStepCountPresent) {
                        int stepCount = characteristic.getIntValue(format, dataIndex);
                        _data.set(4, "STEP COUNT " + stepCount + ", (" + hexString + ")");
                        dataIndex = dataIndex + 2;
                    }

                    if (isActivityPresent) {
                        float activity = characteristic.getIntValue(format, dataIndex)/256.0f;
                        _data.set(5, "ACTIVITY " + activity + ", (" + hexString + ")");
                        dataIndex = dataIndex + 2;
                    }

                    if (isCadencePresent) {
                        int cadence = characteristic.getIntValue(format,dataIndex);
                        _data.set(6, "CADENCE " + cadence + ", (" + hexString + ")");
                    }
                }
            });
        }
    };


    */