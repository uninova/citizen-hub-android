package pt.uninova.s4h.citizenhub.ui;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BluetoothParcelable implements Parcelable {

    public static final Creator<BluetoothParcelable> CREATOR = new Creator<BluetoothParcelable>() {
        @Override
        public BluetoothParcelable createFromParcel(Parcel in) {
            return new BluetoothParcelable(in);
        }

        @Override
        public BluetoothParcelable[] newArray(int size) {
            return new BluetoothParcelable[size];
        }
    };
    public ArrayList<BluetoothDevice> deviceList;


    protected BluetoothParcelable(Parcel in) {
        if (in.readByte() == 0x01) {
            deviceList = new ArrayList<>();
            in.readList(deviceList, BluetoothDevice.class.getClassLoader());
        } else {
            deviceList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (deviceList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(deviceList);
        }
    }
}
