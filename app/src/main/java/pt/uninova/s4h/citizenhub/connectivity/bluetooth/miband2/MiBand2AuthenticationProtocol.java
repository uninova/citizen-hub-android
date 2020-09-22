package pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.*;
import pt.uninova.util.messaging.Observer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

public class MiBand2AuthenticationProtocol extends BluetoothProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.miband2.authentication");

    final private static UUID XIAOMI_MIBAND2_CHARACTERISTIC_AUTH = UUID.fromString("00000009-0000-3512-2118-0009af100700");
    final private static UUID XIAOMI_MIBAND2_DESCRIPTOR_AUTH = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    final private static UUID XIAOMI_MIBAND2_SERVICE_AUTH = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");

    private final SecureRandom keyGenerator;
    private byte[] key;


    public MiBand2AuthenticationProtocol(BluetoothConnection connection) {
        super(ID, connection);

        keyGenerator = new SecureRandom();
        key = new byte[16];

        connection.addDescriptorListener(new BaseDescriptorListener(XIAOMI_MIBAND2_SERVICE_AUTH, XIAOMI_MIBAND2_CHARACTERISTIC_AUTH, BluetoothConnection.ORG_BLUETOOTH_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIGURATION) {
            @Override
            public void onWrite(byte[] value) {
                connection.removeDescriptorListener(this);
                sendNewKey();
            }

            @Override
            public void onWriteFailure(byte[] value, int status) {
                super.onWriteFailure(value, status);
            }
        });

        connection.addCharacteristicListener(new BaseCharacteristicListener(XIAOMI_MIBAND2_SERVICE_AUTH, XIAOMI_MIBAND2_CHARACTERISTIC_AUTH) {
            @Override
            public void onChange(byte[] value) {
                if (value[0] == 0x10 && value[1] == 0x01 && value[2] == 0x01) {
                    connection.writeCharacteristic(
                            XIAOMI_MIBAND2_SERVICE_AUTH,
                            XIAOMI_MIBAND2_CHARACTERISTIC_AUTH,
                            new byte[]{0x02, 0x08});
                } else if (value[0] == 0x10 && value[1] == 0x02 && value[2] == 0x01) {
                    try {
                        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                        SecretKeySpec secret = new SecretKeySpec(key, "AES");

                        cipher.init(Cipher.ENCRYPT_MODE, secret);

                        byte[] handshake = cipher.doFinal(Arrays.copyOfRange(value, 3, 19));
                        byte[] message = new byte[18];

                        message[0] = 0x03;
                        message[1] = 0x08;

                        for (int i = 0; i < handshake.length; i++) {
                            message[i + 2] = handshake[i];
                        }

                        connection.writeCharacteristic(
                                XIAOMI_MIBAND2_SERVICE_AUTH,
                                XIAOMI_MIBAND2_CHARACTERISTIC_AUTH,
                                message);
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                } else if (value[0] == 0x10 && value[1] == 0x03 && value[2] == 0x01) {
                    setState(ProtocolState.ENABLED);
                }
            }
        });

        connection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState>>() {
            @Override
            public void onChanged(StateChangedMessage<BluetoothConnectionState> value) {
                if (value.getNewState() == BluetoothConnectionState.CONNECTED && value.getOldState() == BluetoothConnectionState.DISCONNECTED) {
                    if (getState() == ProtocolState.ENABLED) {
                        enable();
                    }
                }
            }
        });
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
    }

    @Override
    public void enable() {
        getConnection().enableNotifications(XIAOMI_MIBAND2_SERVICE_AUTH, XIAOMI_MIBAND2_CHARACTERISTIC_AUTH);
    }

    private void requestRandomKey() {
        byte[] value = new byte[]{0x01, 0x08};

        getConnection().writeCharacteristic(XIAOMI_MIBAND2_SERVICE_AUTH, XIAOMI_MIBAND2_CHARACTERISTIC_AUTH, value);
    }

    private void sendNewKey() {
        byte[] message = new byte[18];

        keyGenerator.nextBytes(key);

        message[0] = 0x01;
        message[1] = 0x08;

        for (int i = 2; i < message.length; i++) {
            message[i] = key[i - 2];
        }

        getConnection().writeCharacteristic(XIAOMI_MIBAND2_SERVICE_AUTH, XIAOMI_MIBAND2_CHARACTERISTIC_AUTH, message);
    }
}
