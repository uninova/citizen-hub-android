package pt.uninova.s4h.citizenhub;

public class DeviceListItem {
    private int mImageResource;
    private String mTextTitle;
    private String mTextDescription;
    private String mTextNumber;

    public DeviceListItem(int imageResource, String textTitle, String textDescription, String textNumber) {
        mImageResource = imageResource;
        mTextTitle = textTitle;
        mTextDescription = textDescription;
        mTextNumber = textNumber;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmTextTitle() {
        return mTextTitle;
    }

    public String getmTextDescription() {
        return mTextDescription;
    }

    public String getmTextNumber() {
        return mTextNumber;
    }
}
