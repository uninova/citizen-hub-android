package pt.uninova.s4h.citizenhub.connectivity.wearos;

import pt.uninova.s4h.citizenhub.connectivity.AgentFactory;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.messaging.Observer;

public class WearOsAgentFactory implements AgentFactory<WearOSAgent> {

    private final CitizenHubService service;

    public WearOsAgentFactory(CitizenHubService citizenHubService) {
        this.service = citizenHubService;
    }

    @Override
    public void create(String address, Observer<WearOSAgent> observer) {
        WearOSConnection wearOSConnection = service.getWearOSMessageService().connect(address,service);

        wearOSConnection.addConnectionStateChangeListener(new Observer<StateChangedMessage<WearOSConnectionState, WearOSConnection>>() {
            @Override
            public void observe(StateChangedMessage<WearOSConnectionState, WearOSConnection> value) {
                if (value.getNewState() == WearOSConnectionState.READY) {
                    observer.observe(new WearOSAgent(wearOSConnection));
                }
            }
        });
        wearOSConnection.enable();
    }
}
