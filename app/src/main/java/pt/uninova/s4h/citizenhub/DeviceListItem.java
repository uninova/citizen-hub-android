package pt.uninova.s4h.citizenhub;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceListItem {
    private int image;
    private String name;
    private String address;
    private int imageSettings;
    private Device device;

    public DeviceListItem( Device device,int imageResource, int imageSettings)
    {
        image = imageResource;
        this.device = device;
        this.imageSettings = imageSettings;
    }

    public void changeImageResource(int imageResource) {
        image = imageResource; }

    public void changeImageSettings(int imageSettings) {
        this.imageSettings = imageSettings; }

    public int getImageResource() {
        return image;
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
