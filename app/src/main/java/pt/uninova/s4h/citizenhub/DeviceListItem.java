package pt.uninova.s4h.citizenhub;

public class DeviceListItem {
    private int mImageResource;
    private String mTextTitle;
    private String mTextDescription;
    private int mImageSettings;

    public DeviceListItem(int imageResource, String textTitle, String textDescription, int imageSettings)
    {
        mImageResource = imageResource;
        mTextTitle = textTitle;
        mTextDescription = textDescription;
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
        return mTextTitle;
    }

    public String getmTextDescription() {
        return mTextDescription;
    }
}
