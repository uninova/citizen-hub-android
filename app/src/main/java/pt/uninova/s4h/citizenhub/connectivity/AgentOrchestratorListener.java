package pt.uninova.s4h.citizenhub.connectivity;

public interface AgentOrchestratorListener {

    default void onAgentAttached(Device device, Agent agent) {
    }


    default void onDeviceAdded(Device device) {
    }

    default void onDeviceRemoved(Device device) {
    }

}
