package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.util.Log;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.data.BadPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.CaloriesMeasurement;
import pt.uninova.s4h.citizenhub.data.DistanceMeasurement;
import pt.uninova.s4h.citizenhub.data.GoodPostureMeasurement;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.SittingMeasurement;
import pt.uninova.s4h.citizenhub.data.StandingMeasurement;
import pt.uninova.s4h.citizenhub.data.StepCountMeasurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.messaging.Dispatcher;


public class WearOSStepsProtocol extends AbstractMeasuringProtocol {
    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear.steps");
    final private static MeasurementKind channelName = MeasurementKind.STEPS;
    private static final String TAG = "WearOSStepsProtocol";
    CitizenHubService service;

    protected WearOSStepsProtocol(WearOSConnection connection, Dispatcher<Sample> sampleDispatcher, WearOSAgent agent, CitizenHubService service) {
        super(ID, agent,sampleDispatcher);
        Log.d(TAG, "Entered");
        this.service = service;

        connection.addChannelListener(new BaseChannelListener(channelName) {
            @Override
            public void onChange(double value, Date timestamp) {
                final int steps = (int) value;
                final Sample sample = new Sample(getAgent().getSource(),
                        new StepCountMeasurement(steps));
                getSampleDispatcher().dispatch(sample);
            }
        });
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
        service.getWearOSMessageService().sendMessage("WearOSStepsProtocol","disabled");
    }

    @Override
    public void enable() {
        setState(ProtocolState.ENABLED);
        service.getWearOSMessageService().sendMessage("WearOSStepsProtocol","enabled");
    }
}
