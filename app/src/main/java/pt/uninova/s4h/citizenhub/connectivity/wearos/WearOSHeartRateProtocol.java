package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.util.Log;
import android.widget.ListAdapter;

import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.FeatureListItem;
import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
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
                if(value<1000){
                    final Sample sample = new Sample(getAgent().getSource(),
                            new HeartRateMeasurement(heartRate));
                    getSampleDispatcher().dispatch(sample);
                }
                else{
                    if(value==1000)
                    {
                        getAgent().disableMeasurement(MeasurementKind.HEART_RATE);
                    }
                    else if(value==1001)
                    {
                        getAgent().enableMeasurement(MeasurementKind.HEART_RATE);
                    }
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