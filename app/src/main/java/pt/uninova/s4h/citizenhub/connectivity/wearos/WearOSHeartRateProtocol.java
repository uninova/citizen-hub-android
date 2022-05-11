package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.util.Log;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.messaging.Dispatcher;

public class WearOSHeartRateProtocol extends AbstractMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear.heartrate");
    final private static MeasurementKind channelName = MeasurementKind.HEART_RATE;
    final private static int wearProtocolDisable = 1000, wearProtocolEnable = 1001;
    final String TAG = "WearOSHeartRateProtocol";
    CitizenHubService service;

    protected WearOSHeartRateProtocol(WearOSConnection connection, Dispatcher<Sample> sampleDispatcher, WearOSAgent agent, CitizenHubService service) {
        super(ID, agent,sampleDispatcher);
        Log.d(TAG, "Entered");
        this.service = service;

        connection.addChannelListener(new BaseChannelListener(channelName) {
            @Override
            public void onChange(double value, Date timestamp) {
                final int heartRate = (int) value;
                if(value<wearProtocolDisable){
                    final Sample sample = new Sample(getAgent().getSource(),
                            new HeartRateMeasurement(heartRate));
                    getSampleDispatcher().dispatch(sample);
                }
                else{
                    Set<MeasurementKind> enabledMeasurements = getAgent().getEnabledMeasurements();
                    if(value==wearProtocolDisable && enabledMeasurements.contains(MeasurementKind.HEART_RATE))
                        getAgent().disableMeasurement(MeasurementKind.HEART_RATE);
                    else if(value==wearProtocolEnable && !enabledMeasurements.contains(MeasurementKind.HEART_RATE))
                        getAgent().enableMeasurement(MeasurementKind.HEART_RATE);
                }
            }
        });
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
        service.getWearOSMessageService().sendMessage("WearOSHeartRateProtocol","false");
    }

    @Override
    public void enable() {
        setState(ProtocolState.ENABLED);
        service.getWearOSMessageService().sendMessage("WearOSHeartRateProtocol","true");
    }
}