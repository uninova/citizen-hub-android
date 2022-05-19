package pt.uninova.s4h.citizenhub.connectivity;


public interface AgentListener {

    default void onMeasurementDisabled(Agent agent, int measurementKind) {
    }

    default void onMeasurementEnabled(Agent agent, int measurementKind) {
    }

    default void onProtocolDisabled(Agent agent, Protocol protocol) {
    }

    default void onProtocolEnabled(Agent agent, Protocol protocol) {
    }
}
