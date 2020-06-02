package pt.uninova.util.events;

public interface EventListener<S, M extends EventMessage> {

    void onEvent(S sender, M message);

}
