package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;


public class WearOSStepsProtocol extends AbstractMeasuringProtocol {
    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear.steps");
    final private static String channelName = "STEPS";
    private static final String TAG = "WearOSStepsProtocol";
    private final WearOSConnection wearOSConnection;

    protected WearOSStepsProtocol(WearOSConnection connection) {
        super(ID);
        wearOSConnection= connection;
        Log.d(TAG, "Entered"  );

        connection.addChannelListener(new BaseChannelListener(channelName) {
            @Override
            public void onChange(double value, Date timestamp) {

                getMeasurementDispatcher().dispatch(new Measurement(timestamp, MeasurementKind.STEPS, value));
                Log.d(TAG, "dispatch " + timestamp + " and " + value);
            }
        });
    }

    public void disable() {
        setState(ProtocolState.DISABLED);
    }


    public void enable(double value, Timestamp timestamp) {
        setState(ProtocolState.ENABLED);
    }
}
