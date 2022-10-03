package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentFactory;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.miband2.MiBand2Agent;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class BluetoothAgentFactory implements AgentFactory<BluetoothAgent> {

    private final Context context;
    private Class<?> agentClass;

    public BluetoothAgentFactory(Context context) {
        this.context = context;
    }

    public void create(String address, Observer<BluetoothAgent> observer) {
        BluetoothManager bluetoothManager = context.getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        BluetoothConnection bluetoothConnection = new BluetoothConnection(bluetoothDevice);

        bluetoothConnection.addConnectionStateChangeListener(new Observer<StateChangedMessage<BluetoothConnectionState, BluetoothConnection>>() {

            @Override
            public void observe(StateChangedMessage<BluetoothConnectionState, BluetoothConnection> value) {
                if (value.getNewState() == BluetoothConnectionState.READY) {
                    final BluetoothConnection source = value.getSource();

                    value.getSource().removeConnectionStateChangeListener(this);

                    BluetoothAgentMatchers agentMatcher = new BluetoothAgentMatchers(source);
                    agentClass = agentMatcher.runAgentMatchers();
                    if (agentClass != null) {
                        initAgent(agentClass, source, observer);
                    }
                }
            }
        });

        bluetoothConnection.connect();
    }

    private void initAgent(Class<?> agentClass, BluetoothConnection source, Observer observer) {
        try {

            Class<?> agent = Class.forName(agentClass.getName());
            Class<?>[] parameters = new Class[]{BluetoothConnection.class, Context.class};
            Constructor<?> constructor = agent.getConstructor(parameters);

            Object o = constructor.newInstance(source, context);
            observer.observe((BluetoothAgent) o);

        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public List<UUID> serviceToUUIDList(List<BluetoothGattService> serviceList) {
        List<UUID> uuidList = new ArrayList<>();
        for (BluetoothGattService service : serviceList
        ) {
            uuidList.add(service.getUuid());
        }
        System.out.println("Lista: " + uuidList);
        return uuidList;
    }

    @Override
    public void create(String address, Class<?> c, Observer<BluetoothAgent> observer) {
        try {
            final Constructor<?> constructor = c.getConstructor(BluetoothConnection.class, Context.class);

            final BluetoothManager bluetoothManager = context.getSystemService(BluetoothManager.class);
            final BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            final BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            final BluetoothConnection bluetoothConnection = new BluetoothConnection(bluetoothDevice);

            observer.observe((BluetoothAgent) constructor.newInstance(bluetoothConnection, context));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
