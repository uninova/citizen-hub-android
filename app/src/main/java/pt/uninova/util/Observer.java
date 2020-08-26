package pt.uninova.util;

public interface Observer<T> {

    void onChange(T v);

}
