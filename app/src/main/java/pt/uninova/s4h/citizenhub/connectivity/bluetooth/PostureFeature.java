package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.service.DeviceManager;

public class PostureFeature extends BluetoothFeature {

    private static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("");
    private static UUID postureServiceUUID = UUID.fromString("");
    private static UUID postureCharacteristicUUID = UUID.fromString("");
    private static UUID postureDescriptorUUID;
    private static UUID uuid = DeviceManager.namespaceGenerator().getUUID("bluetooth.generic.posture_measurement");

public  PostureFeature(BluetoothConnection connection, UUID id) {
        super(connection, id);
        }


@Override
public UUID getId() {
        return uuid;
        }

@Override
public boolean enable() {
        try {
        getConnection().enableNotifications(postureServiceUUID, postureCharacteristicUUID);
        // observer -> measurementRepository.add;
        return true;
        } catch (Exception e) {
        return false;
        }
        }

@Override
public boolean disable() {
        try {
        getConnection().disableNotifications(postureServiceUUID, postureCharacteristicUUID);
        return true;
        } catch (Exception e) {
        return false;
        // measurementRepository.add;
        }
        }
        }
