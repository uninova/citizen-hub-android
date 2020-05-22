package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceManager implements AutoCloseable {

    private final Map<UUID, Device> deviceMap;
    private final Collection<DeviceManagerListener> deviceManagerListeners;

    public DeviceManager() {
        deviceMap = new ConcurrentHashMap<>();
        deviceManagerListeners = new LinkedList<>();
    }

    public void add(final Device device) {
        synchronized (deviceMap) {
            if (deviceMap.containsKey(device.getId()))
                return;

            deviceMap.put(device.getId(), device);
        }

        synchronized (deviceManagerListeners) {
            for (DeviceManagerListener i : deviceManagerListeners) {
                i.onDeviceAdded(device);
            }
        }
    }

    public void addListener(DeviceManagerListener listener) {
        synchronized (deviceManagerListeners) {
            deviceManagerListeners.add(listener);
        }
    }

    @Override
    public void close() {
        deviceManagerListeners.clear();
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

        synchronized (deviceManagerListeners) {
            for (DeviceManagerListener i : deviceManagerListeners) {
                i.onDeviceRemoved(device);
            }
        }
    }

    public void removeListener(DeviceManagerListener listener) {
        synchronized (deviceManagerListeners) {
            deviceManagerListeners.remove(listener);
        }
    }

}
