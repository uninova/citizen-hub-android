package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.util.messaging.Observer;

import java.util.Set;

public interface MeasuringProtocol extends Protocol {

    void addSampleObserver(Observer<Sample> observer);

    void clearSampleObservers();

    void removeSampleObserver(Observer<Sample> observer);

}
