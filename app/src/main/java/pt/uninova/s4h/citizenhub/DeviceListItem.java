package pt.uninova.s4h.citizenhub;

import java.util.Objects;

import pt.uninova.s4h.citizenhub.data.Device;

public class DeviceListItem implements Comparable<DeviceListItem> {

    private final Device device;
    private int imageResource;

    public DeviceListItem(Device device, int imageResource) {
        this.device = device;
        this.imageResource = imageResource;
    }

    @Override
    public int compareTo(DeviceListItem o) {
        return device.compareTo(o.getDevice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        DeviceListItem that = (DeviceListItem) o;

        return device.equals(that.device);
    }

    public Device getDevice() {
        return device;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(device);
    }
}
