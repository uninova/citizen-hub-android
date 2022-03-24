package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.StepCountMeasurement;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;
import pt.uninova.util.messaging.Dispatcher;

public class WearOSHeartRateProtocol extends AbstractMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear.heartrate");
    final private static MeasurementKind channelName = MeasurementKind.HEART_RATE;
    final String TAG = "WearOSHeartRateProtocol";
    CitizenHubService service;
    String citizenhubPath = "/citizenhub_path_";


    protected WearOSHeartRateProtocol(WearOSConnection connection, Dispatcher<Sample> sampleDispatcher, WearOSAgent agent, CitizenHubService service) {
        super(ID, agent,sampleDispatcher);
        Log.d(TAG, "Entered");
        this.service = service;

        connection.addChannelListener(new BaseChannelListener(channelName) {
            @Override
            public void onChange(double value, Date timestamp) {
                final int heartRate = (int) value;
                final Sample sample = new Sample(getAgent().getSource(),
                        new HeartRateMeasurement(heartRate));
                getSampleDispatcher().dispatch(sample);
            }
        });
    }

    @Override
    public void disable() {
        setState(ProtocolState.DISABLED);
        service.getWearOSMessageService().sendMessage("WearOSHeartRateProtocol","disabled");
    }

    @Override
    public void enable() {
        setState(ProtocolState.ENABLED);
        service.getWearOSMessageService().sendMessage("WearOSHeartRateProtocol","enabled");
    }
}