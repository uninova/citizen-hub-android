package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public abstract class BluetoothMeasuringProtocol extends BluetoothProtocol implements MeasuringProtocol {

    final private Dispatcher<Measurement> measurementDispatcher;

    public BluetoothMeasuringProtocol(UUID id, BluetoothConnection connection) {
        super(id, connection);

        measurementDispatcher = new Dispatcher<>();
    }

    protected Dispatcher<Measurement> getMeasurementDispatcher() {
        return measurementDispatcher;
    }

    @Override
    public Set<Observer<Measurement>> getMeasurementObservers() {
        return measurementDispatcher.getObservers();
    }

}
