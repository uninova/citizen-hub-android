package pt.uninova.util.messaging;

public interface Observer<T> {

    void onChanged(T value);

}
