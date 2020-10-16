package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

public class HexoSkinDataConverter {

    public static Integer getIntValue(int formatType, int offset, byte[] value) {
        if(value!=null) {
            if ((offset + getTypeLen(formatType)) > value.length) return null;

            switch (formatType) {
                case FORMAT_UINT8:
                    return unsignedByteToInt(value[offset]);

                case FORMAT_UINT16:
                    return unsignedBytesToInt(value[offset], value[offset + 1]);
            }
        }

        return null;
    }

    private static int getTypeLen(int formatType) {
        return formatType & 0xF;
    }

    private static int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    private static int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }

}
