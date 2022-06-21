package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import pt.uninova.s4h.citizenhub.connectivity.AgentFactory;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.and.BloodPressureMonitorAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole.DigitsoleAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin.HexoSkinAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzBodyProtocol;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture.KbzPostureAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.uprightgo2.UprightGo2Agent;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class BluetoothAgentFactory implements AgentFactory<BluetoothAgent> {

    private final Context context;

    public BluetoothAgentFactory(Context context) {
        this.context = context;
    }

    public void create(String address, Observer<BluetoothAgent> observer) {
        BluetoothManager bluetoothManager = context.getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        BluetoothConnection bluetoothConnection = new BluetoothConnection();

        bluetoothConnection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {

            @Override
            public void observe(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {
                if (value.getNewState() == BluetoothConnectionState.READY) {
                    final BluetoothConnection source = value.getSource();
                    final BluetoothDevice device = source.getDevice();

                    value.getSource().removeConnectionStateChangeListener(this);

                    final String name = device.getName();

                    if (name.startsWith("HX")) {
                        observer.observe(new HexoSkinAgent(source));
                    } else if (name.startsWith("MI")) {
                        observer.observe(new MiBand2Agent(source));
                    } else if (source.hasService(KbzBodyProtocol.KBZ_SERVICE)) {
                        observer.observe(new KbzPostureAgent(source));
                    } else if (name.startsWith("UprightGO2")) {
                        observer.observe(new UprightGo2Agent(source));
                    } else if (name.startsWith("A&D")) {
                        observer.observe(new BloodPressureMonitorAgent(source));
                    } else if (name.startsWith("ZTEZ")) {
                        observer.observe(new DigitsoleAgent(source, context));
                    } else {
                        observer.observe(null);
                    }
                }
            }
        });

        if (bluetoothDevice.getName().startsWith("HX"))
            bluetoothConnection.connect(bluetoothDevice, BluetoothDevice.TRANSPORT_AUTO);
        else
            bluetoothConnection.connect(bluetoothDevice);
    }
}
