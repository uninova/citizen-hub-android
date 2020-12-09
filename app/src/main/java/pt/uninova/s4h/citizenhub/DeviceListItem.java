package pt.uninova.s4h.citizenhub;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceListItem {
    private int image;
    private String name;
    private String address;
    private int imageSettings;
    private Device device;

    public DeviceListItem(int imageResource, Device device, int imageSettings)
    {
        image = imageResource;
        this.device = device;
        this.imageSettings = imageSettings;
    }

    /*
       public Agent(String name, String address, String type, String state) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.state = state;
    }*/

    public void changeImageResource(int imageResource) {
        image = imageResource; }

    public void changeImageSettings(int imageSettings) {
        this.imageSettings = imageSettings; }

    public int getImageResource() {
        return image;
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
