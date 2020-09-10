package pt.uninova.s4h.citizenhub;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceListItem {
    private int mImageResource;
    private String mTextTitle;
    private String mTextDescription;
    private int mImageSettings;
    private Device device;

    public DeviceListItem(int imageResource, Device device, int imageSettings)
    {
        mImageResource = imageResource;
        this.device = device;
        mImageSettings = imageSettings;
    }

    /*
       public Device(String name, String address, String type, String state) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.state = state;
    }*/

    public void changeImageResource(int imageResource) {mImageResource = imageResource; }

    public void changeImageSettings(int imageSettings) {mImageSettings = imageSettings; }

    public int getmImageResource() {
        return mImageResource;
    }

    public int getmImageSettings() {
        return mImageSettings;
    }

    public String getmTextTitle(){
        return device.getName();
    }

    public Device getDevice() {return device; }

    public String getmTextDescription() {
        return device.getAddress();
    }
}
