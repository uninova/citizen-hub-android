package pt.uninova.s4h.citizenhub.util.messaging;

public interface Observer<T> {

    void observe(T value);

}
