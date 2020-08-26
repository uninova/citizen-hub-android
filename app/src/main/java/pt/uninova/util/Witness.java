package pt.uninova.util;

public interface Witness<T> {

    void onEvent(T event);

}
