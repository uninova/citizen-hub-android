package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.util.events.EventDispatcher;
import pt.uninova.util.events.GenericEventMessage;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceManager implements AutoCloseable {

    private final Map<UUID, Device> deviceMap;

    private final EventDispatcher<DeviceManager, GenericEventMessage<Device>> onDeviceAdded;
    private final EventDispatcher<DeviceManager, GenericEventMessage<Device>> onDeviceRemoved;

    public DeviceManager() {
        deviceMap = new ConcurrentHashMap<>();
        onDeviceAdded = new EventDispatcher<>(this);
        onDeviceRemoved = new EventDispatcher<>(this);
    }

    public void add(final Device device) {
        synchronized (deviceMap) {
            if (deviceMap.containsKey(device.getId()))
                return;

            deviceMap.put(device.getId(), device);
        }

        onDeviceAdded.dispatch(new GenericEventMessage<Device>(device));
    }

    public EventDispatcher<DeviceManager, GenericEventMessage<Device>> onDeviceAdded() {
        return onDeviceAdded;
    }

    public EventDispatcher<DeviceManager, GenericEventMessage<Device>> onDeviceRemoved() {
        return onDeviceRemoved;
    }

    @Override
    public void close() {
        onDeviceAdded.clearListeners();
        onDeviceRemoved.clearListeners();
        deviceMap.clear();
    }

    public Device get(UUID id) {
        return deviceMap.get(id);
    }

    public Collection<Device> getAll() {
        return deviceMap.values();
    }

    public void remove(Device device) {
        remove(device.getId());
    }

    public void remove(UUID id) {
        final Device device;

        synchronized (deviceMap) {
            if (!deviceMap.containsKey(id))
                return;

            device = deviceMap.get(id);
            deviceMap.remove(id);
        }

        onDeviceRemoved.dispatch(new GenericEventMessage<Device>(device));
    }
}
