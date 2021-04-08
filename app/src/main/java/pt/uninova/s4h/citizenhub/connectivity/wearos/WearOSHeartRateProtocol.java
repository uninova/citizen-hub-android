package pt.uninova.s4h.citizenhub.connectivity.wearos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AbstractMeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.ProtocolState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BaseCharacteristicListener;
import pt.uninova.s4h.citizenhub.persistence.Measurement;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

public class WearOSHeartRateProtocol extends AbstractMeasuringProtocol {

    final public static UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("wearos.wear.heartrate");
    final private static MeasurementKind channelName = MeasurementKind.HEART_RATE;

    final String TAG = "WearOSHeartRateProtocol";
    private final WearOSConnection wearOSConnection;


    protected WearOSHeartRateProtocol(WearOSConnection connection) {
        super(ID);
        wearOSConnection= connection;

        Log.d(TAG, "Entered"  );

        connection.addChannelListener(new BaseChannelListener(channelName) {
            @Override
            public void onChange(double value, Date timestamp) {

                    getMeasurementDispatcher().dispatch(new Measurement(timestamp, MeasurementKind.HEART_RATE, value));
                    //Log.d(TAG, "dispatch " + timestamp + " and " + value);
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
