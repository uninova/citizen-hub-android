package pt.uninova.s4h.citizenhub;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceListItem {
    private int imageResource;
    private String name;
    private String address;
    private int imageSettings;
    private Device device;

    public DeviceListItem(Device device, int imageResource, int imageSettings) {
        this.imageResource = imageResource;
        this.device = device;
        this.imageSettings = imageSettings;
    }

    public DeviceListItem(Device device) {
        this.imageResource = R.drawable.ic_watch,
        this.device = device;
        this.imageSettings = R.drawable.ic_settings;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setImageSettings(int imageSettings) {
        this.imageSettings = imageSettings;
    }

    public int getImageSettings() {
        return imageSettings;
    }

    public String getName(){
        return device.getName();
    }

    public Device getDevice() {return device; }

    public String getAddress() {
        return device.getAddress();
    }
}
