package pt.uninova.s4h.citizenhub;

import pt.uninova.s4h.citizenhub.persistence.Device;

public class DeviceListItem {
    private int mImageResource;
    private int mImageSettings;
    private Device device;

    public DeviceListItem(int imageResource, Device device)
    {
        mImageResource = imageResource;
        this.device = device;
    }

    public DeviceListItem(int imageResource, Device device, int imageSettings)
    {
        mImageResource = imageResource;
        this.device = device;
        mImageSettings = imageSettings;
    }

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
