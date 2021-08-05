package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public abstract class BluetoothMeasuringProtocol extends BluetoothProtocol implements MeasuringProtocol {

    private final Dispatcher<Measurement> measurementDispatcher;

    public BluetoothMeasuringProtocol(UUID id, BluetoothConnection connection, Class<?> agent) {
        this(id, connection, new Dispatcher<>(), agent);
    }

    public BluetoothMeasuringProtocol(UUID id, BluetoothConnection connection, Dispatcher<Measurement> dispatcher, Class<?> agent) {
        super(id, connection, agent);

        measurementDispatcher = dispatcher;
    }

    @Override
    public void close() {
        super.close();
    }

    protected Dispatcher<Measurement> getMeasurementDispatcher() {
        return measurementDispatcher;
    }

    @Override
    public Set<Observer<Measurement>> getMeasurementObservers() {
        return measurementDispatcher.getObservers();
    }

}
