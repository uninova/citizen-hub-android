package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.uninova.s4h.citizenhub.connectivity.Connection;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.util.Pair;
import pt.uninova.util.Triple;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public class BluetoothConnection extends BluetoothGattCallback implements Connection {

    public final static UUID ORG_BLUETOOTH_SERVICE_HEART_RATE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");

    public final static UUID ORG_BLUETOOTH_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIGURATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private final Queue<Runnable> runnables;
    private final Map<Pair<UUID, UUID>, Set<CharacteristicListener>> characteristicListenerMap;
    private final Map<Triple<UUID, UUID, UUID>, Set<DescriptorListener>> descriptorListenerMap;
    private final Dispatcher<StateChangedMessage<BluetoothConnectionState>> stateChangedMessageDispatcher;

    private BluetoothGatt gatt;

    private BluetoothConnectionState state;

    public BluetoothConnection() {
        runnables = new ConcurrentLinkedQueue<>();
        characteristicListenerMap = new ConcurrentHashMap<>();
        descriptorListenerMap = new ConcurrentHashMap<>();
        stateChangedMessageDispatcher = new Dispatcher<>();
        state = BluetoothConnectionState.DISCONNECTED;
    }

    public void addCharacteristicListener(CharacteristicListener listener) {
        final Pair<UUID, UUID> key = characteristicKey(listener);

        synchronized (characteristicListenerMap) {
            if (!characteristicListenerMap.containsKey(key)) {
                characteristicListenerMap.put(key, Collections.newSetFromMap(new ConcurrentHashMap<CharacteristicListener, Boolean>()));
            }
        }

        characteristicListenerMap.get(key).add(listener);
    }

    public void addConnectionStateChangeListener(Observer<StateChangedMessage<BluetoothConnectionState>> listener) {
        stateChangedMessageDispatcher.getObservers().add(listener);
    }

    public void addDescriptorListener(DescriptorListener listener) {
        final Triple<UUID, UUID, UUID> key = descriptorKey(listener);

        synchronized (descriptorListenerMap) {
            if (!descriptorListenerMap.containsKey(key)) {
                descriptorListenerMap.put(key, Collections.newSetFromMap(new ConcurrentHashMap<DescriptorListener, Boolean>()));
            }
        }

        descriptorListenerMap.get(key).add(listener);
    }

    private Pair<UUID, UUID> characteristicKey(BluetoothGattCharacteristic characteristic) {
        return characteristicKey(characteristic.getService().getUuid(), characteristic.getUuid());
    }

    private Pair<UUID, UUID> characteristicKey(CharacteristicListener listener) {
        return characteristicKey(listener.getServiceUuid(), listener.getCharacteristicUuid());
    }

    private Pair<UUID, UUID> characteristicKey(UUID serviceUuid, UUID characteristicUuid) {
        return new Pair<>(serviceUuid, characteristicUuid);
    }

    public void close() {
        characteristicListenerMap.clear();
        descriptorListenerMap.clear();
        runnables.clear();

        stateChangedMessageDispatcher.close();

        gatt.close();
    }

    private Triple<UUID, UUID, UUID> descriptorKey(BluetoothGattDescriptor descriptor) {
        return descriptorKey(descriptor.getCharacteristic().getService().getUuid(), descriptor.getCharacteristic().getUuid(), descriptor.getUuid());
    }

    private Triple<UUID, UUID, UUID> descriptorKey(DescriptorListener listener) {
        return descriptorKey(listener.getServiceUuid(), listener.getCharacteristicUuid(), listener.getDescriptorUuid());
    }

    private Triple<UUID, UUID, UUID> descriptorKey(final UUID serviceUuid, final UUID characteristicUuid, final UUID descriptorUuid) {
        return new Triple<>(serviceUuid, characteristicUuid, descriptorUuid);
    }

    public void disableNotifications(final UUID serviceUuid, final UUID characteristicUuid) {
        final BluetoothGattCharacteristic characteristic = gatt.getService(serviceUuid).getCharacteristic(characteristicUuid);

        gatt.setCharacteristicNotification(characteristic, false);
        writeDescriptor(serviceUuid, characteristicUuid, ORG_BLUETOOTH_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIGURATION, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
    }

    public void enableNotifications(final UUID serviceUuid, final UUID characteristicUuid) {
        final BluetoothGattCharacteristic characteristic = gatt.getService(serviceUuid).getCharacteristic(characteristicUuid);

        gatt.setCharacteristicNotification(characteristic, true);
        writeDescriptor(serviceUuid, characteristicUuid, ORG_BLUETOOTH_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIGURATION, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    }

    public BluetoothDevice getDevice() {
        return gatt.getDevice();
    }

    public List<BluetoothGattService> getServices() {
        return gatt.getServices();
    }

    public boolean hasService(UUID uuid) {
        return gatt.getService(uuid) != null;
    }

    private synchronized void next() {
        runnables.poll();

        if (!runnables.isEmpty()) {
            runnables.element().run();
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        final Pair<UUID, UUID> key = characteristicKey(characteristic);

        if (characteristicListenerMap.containsKey(key)) {
            for (CharacteristicListener i : characteristicListenerMap.get(key)) {
                i.onChange(characteristic.getValue());
            }
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        final Pair<UUID, UUID> key = characteristicKey(characteristic);

        if (characteristicListenerMap.containsKey(key)) {
            for (CharacteristicListener i : characteristicListenerMap.get(key)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    i.onRead(characteristic.getValue());
                } else {
                    i.onReadFailure(characteristic.getValue(), status);
                }
            }
        }

        next();
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        final Pair<UUID, UUID> key = characteristicKey(characteristic);

        if (characteristicListenerMap.containsKey(key)) {
            for (CharacteristicListener i : characteristicListenerMap.get(key)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    i.onWrite(characteristic.getValue());
                } else {
                    i.onWriteFailure(characteristic.getValue(), status);
                }
            }
        }

        next();
    }

    @Override
    public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
        System.out.println("ONCONNECTIONSTATE" + " " + status + " " + newState);

        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                if (this.gatt == null) {
                    this.gatt = gatt;
                    setState(BluetoothConnectionState.CONNECTED);

                    push(new Runnable() {
                        @Override
                        public void run() {
                            gatt.discoverServices();
                        }
                    });
                } else {
                    setState(BluetoothConnectionState.READY);
                }
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                setState(BluetoothConnectionState.DISCONNECTED);
            }
        }
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        final Triple<UUID, UUID, UUID> key = descriptorKey(descriptor);

        if (descriptorListenerMap.containsKey(key)) {
            for (DescriptorListener i : descriptorListenerMap.get(key)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    i.onRead(descriptor.getValue());
                } else {
                    i.onReadFailure(descriptor.getValue(), status);
                }
            }
        }

        next();
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        final Triple<UUID, UUID, UUID> key = descriptorKey(descriptor);
        if (descriptorListenerMap.containsKey(key)) {
            for (DescriptorListener i : descriptorListenerMap.get(key)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    i.onWrite(descriptor.getValue());
                } else {
                    i.onWriteFailure(descriptor.getValue(), status);
                }
            }
        }

        next();
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if (status == BluetoothGatt.GATT_SUCCESS) {
            setState(BluetoothConnectionState.READY);
        }

        next();
    }

    private synchronized void push(Runnable runnable) {
        runnables.add(runnable);

        if (runnables.size() == 1) {
            runnables.element().run();
        }
    }

    public void readCharacteristic(final UUID serviceUuid, final UUID characteristicUuid) {
        push(new Runnable() {
            @Override
            public void run() {
                final BluetoothGattCharacteristic characteristic = gatt.getService(serviceUuid).getCharacteristic(characteristicUuid);

                gatt.readCharacteristic(characteristic);
            }
        });
    }

    public void readDescriptor(final UUID serviceUuid, final UUID characteristicUuid, final UUID descriptorUuid) {
        push(new Runnable() {
            @Override
            public void run() {
                final BluetoothGattDescriptor descriptor = gatt.getService(serviceUuid).getCharacteristic(characteristicUuid).getDescriptor(descriptorUuid);

                gatt.readDescriptor(descriptor);
            }
        });
    }

    public void removeCharacteristicListener(CharacteristicListener listener) {
        final Pair<UUID, UUID> key = characteristicKey(listener);

        if (characteristicListenerMap.containsKey(key)) {
            final Set<CharacteristicListener> listeners = characteristicListenerMap.get(key);

            listeners.remove(listener);

            if (listeners.isEmpty()) {
                characteristicListenerMap.remove(key);
            }
        }
    }

    public void removeConnectionStateChangeListener(Observer<StateChangedMessage<BluetoothConnectionState>> listener) {
        stateChangedMessageDispatcher.getObservers().remove(listener);
    }

    public void removeDescriptorListener(DescriptorListener listener) {
        final Triple<UUID, UUID, UUID> key = descriptorKey(listener);

        if (descriptorListenerMap.containsKey(key)) {
            final Set<DescriptorListener> listeners = descriptorListenerMap.get(key);

            listeners.remove(listener);

            if (listeners.isEmpty()) {
                descriptorListenerMap.remove(key);
            }
        }
    }

    private void setState(BluetoothConnectionState value) {
        if (value != state) {
            final BluetoothConnectionState oldValue = state;

            this.state = value;

            stateChangedMessageDispatcher.dispatch(new StateChangedMessage<>(state, oldValue));
        }
    }

    public void writeCharacteristic(final UUID serviceUuid, final UUID characteristicUuid, final byte[] value) {
        push(new Runnable() {
            @Override
            public void run() {
                final BluetoothGattCharacteristic characteristic = gatt.getService(serviceUuid).getCharacteristic(characteristicUuid);

                characteristic.setValue(value);
                gatt.writeCharacteristic(characteristic);
            }
        });
    }

    public void writeDescriptor(final UUID serviceUuid, final UUID characteristicUuid, final UUID descriptorUuid, final byte[] value) {
        push(new Runnable() {
            @Override
            public void run() {
                final BluetoothGattDescriptor descriptor = gatt.getService(serviceUuid).getCharacteristic(characteristicUuid).getDescriptor(descriptorUuid);

                descriptor.setValue(value);
                gatt.writeDescriptor(descriptor);
            }
        });
    }

    @Override
    public ConnectionKind getConnectionKind() {
        return ConnectionKind.BLUETOOTH;
    }

    @Override
    public String getAddress() {
        return getDevice().getAddress();
    }
}
