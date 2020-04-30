package pt.uninova.s4h.citizenhub.ui;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

interface BackgroundServiceInterface extends ComponentCallbacks2 {

    void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                       boolean enabled);



    IBinder onBind(Intent intent);

    void SendList();

    List<BluetoothDevice> CheckConnectedDevices (BluetoothManager bluetoothManager);

    void CheckDeviceConnection(BluetoothManager bluetoothManager, BluetoothDevice device);

    void scanDevice(boolean enable);

    void getData(BluetoothDevice device);

    void PreScan();
}
