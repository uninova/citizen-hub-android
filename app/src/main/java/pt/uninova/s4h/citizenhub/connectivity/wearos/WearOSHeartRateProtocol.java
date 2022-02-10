package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.data.HeartRateMeasurement;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.StepCountMeasurement;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class WearOSHeartRateProtocol extends AbstractMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear.heartrate");
    final private static MeasurementKind channelName = MeasurementKind.HEART_RATE;

    final String TAG = "WearOSHeartRateProtocol";
    private final WearOSConnection connection;


    protected WearOSHeartRateProtocol(WearOSConnection connection, WearOSAgent agent) {
        super(ID, agent);
        this.connection = connection;

        Log.d(TAG, "Entered");

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
    }

    @Override
    public void enable() {
        setState(ProtocolState.ENABLED);
    }

}
