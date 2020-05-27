package pt.uninova.util;

public interface EventListener<T extends Event> {

    void onEvent(T e);

}
