package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.util.messaging.Dispatcher;
import pt.uninova.util.messaging.Observer;

import java.util.Set;
import java.util.UUID;

public abstract class AbstractMeasuringProtocol extends AbstractProtocol implements MeasuringProtocol {

    final private Dispatcher<Measurement> measurementDispatcher;

    protected AbstractMeasuringProtocol(UUID id) {
        super(id);

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
