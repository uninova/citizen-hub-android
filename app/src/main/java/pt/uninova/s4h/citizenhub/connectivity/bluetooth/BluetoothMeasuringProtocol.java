package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

public abstract class BluetoothMeasuringProtocol extends BluetoothProtocol implements MeasuringProtocol {

    private final Dispatcher<Sample> sampleDispatcher;

    public BluetoothMeasuringProtocol(UUID id, BluetoothConnection connection, BluetoothAgent agent) {
        this(id, connection, new Dispatcher<>(), agent);
    }

    public BluetoothMeasuringProtocol(UUID id, BluetoothConnection connection, Dispatcher<Sample> dispatcher, BluetoothAgent agent) {
        super(id, connection, agent);

        sampleDispatcher = dispatcher;
    }

    @Override
    public void addSampleObserver(Observer<Sample> observer) {
        this.sampleDispatcher.addObserver(observer);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void clearSampleObservers() {
        sampleDispatcher.close();
    }

    protected Dispatcher<Sample> getSampleDispatcher() {
        return sampleDispatcher;
    }

    @Override
    public void removeSampleObserver(Observer<Sample> observer) {
        this.sampleDispatcher.removeObserver(observer);
    }
}
