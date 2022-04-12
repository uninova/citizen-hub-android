package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public interface AgentListener {

    default void onMeasurementDisabled(Agent agent, MeasurementKind measurementKind) {
    }

    default void onMeasurementEnabled(Agent agent, MeasurementKind measurementKind) {
    }

    default void onProtocolDisabled(Agent agent, Protocol protocol) {
    }

    default void onProtocolEnabled(Agent agent, Protocol protocol) {
    }
}
