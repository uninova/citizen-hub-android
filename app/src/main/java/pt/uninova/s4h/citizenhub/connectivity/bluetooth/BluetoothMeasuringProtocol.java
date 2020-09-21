package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public abstract class BluetoothMeasuringProtocol extends BluetoothProtocol implements MeasuringProtocol {

    final private Dispatcher<Measurement> measurementDispatcherDispatcher;

    public BluetoothMeasuringProtocol(UUID id, BluetoothConnection connection) {
        super(id, connection);

        measurementDispatcherDispatcher = new Dispatcher<>();
    }

    @Override
    public Set<Observer<Measurement>> getMeasurementObservers() {
        return measurementDispatcherDispatcher.getObservers();
    }

}
