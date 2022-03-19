package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.util.messaging.Observer;

public interface MeasuringProtocol extends Protocol {

    void addSampleObserver(Observer<Sample> observer);

    void clearSampleObservers();

    void removeSampleObserver(Observer<Sample> observer);

}
