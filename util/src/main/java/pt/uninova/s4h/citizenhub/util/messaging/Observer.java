package pt.uninova.util.messaging;

public interface Observer<T> {

    void observe(T value);

}
