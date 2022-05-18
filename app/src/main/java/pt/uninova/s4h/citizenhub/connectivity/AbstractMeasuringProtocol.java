package pt.uninova.s4h.citizenhub.connectivity;

import java.util.UUID;

import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.util.messaging.Dispatcher;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public abstract class AbstractMeasuringProtocol extends AbstractProtocol implements MeasuringProtocol {

    final private Dispatcher<Sample> sampleDispatcher;

    protected AbstractMeasuringProtocol(UUID id, Agent agent) {
        this(id, agent, new Dispatcher<>());
    }

    protected AbstractMeasuringProtocol(UUID id, Agent agent, Dispatcher<Sample> dispatcher) {
        super(id, agent);

        this.sampleDispatcher = dispatcher;
    }

    @Override
    public void addSampleObserver(Observer<Sample> observer) {
        this.sampleDispatcher.addObserver(observer);
    }

    protected Dispatcher<Sample> getSampleDispatcher() {
        return sampleDispatcher;
    }

    @Override
    public void removeSampleObserver(Observer<Sample> observer) {
        this.sampleDispatcher.removeObserver(observer);
    }

    @Override
    public void clearSampleObservers() {
        this.sampleDispatcher.close();
    }

}
