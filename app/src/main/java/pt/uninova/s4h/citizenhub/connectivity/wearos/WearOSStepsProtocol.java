package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.data.StepCountMeasurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Dispatcher;


public class WearOSStepsProtocol extends AbstractMeasuringProtocol {
    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear.steps");
    final private static MeasurementKind channelName = MeasurementKind.STEPS;
    private static final String TAG = "WearOSStepsProtocol";

    protected WearOSStepsProtocol(WearOSConnection connection, Dispatcher<Sample> sampleDispatcher, WearOSAgent agent) {
        super(ID, agent,sampleDispatcher);
        Log.d(TAG, "Entered");

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
}
