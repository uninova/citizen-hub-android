package pt.uninova.s4h.citizenhub.connectivity.bluetooth.hexoskin;

import android.content.Context;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.RoomSettingsManager;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.data.Measurement;

public class HexoSkinAgent extends BluetoothAgent {

    static public final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.hexoskin");

    static private final Set<Integer> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Measurement.TYPE_STEPS_SNAPSHOT,
            Measurement.TYPE_HEART_RATE,
            Measurement.TYPE_BREATHING_RATE
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            HexoSkinAccelerometerProtocol.ID,
            HexoSkinHeartRateProtocol.ID,
            HexoSkinRespirationProtocol.ID
    )));

    public HexoSkinAgent(BluetoothConnection connection, Context context) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection, new RoomSettingsManager(context, connection.getAddress()));
    }

    @Override
    public Set<Integer> getSupportedMeasurements() {
        return HexoSkinAgent.supportedMeasurementKinds;
    }

    @Override
    public List<Fragment> getConfigurationFragments() {
        return null;
    }

    @Override
    public List<Integer> getConfigurationButtonResources() {
        return null;
    }

    @Override
    public List<MenuItem.OnMenuItemClickListener> getConfigurationButtonClickListener() {
        return null;
    }

    @Override
    public boolean hasConfigurationButtons() {
        return false;
    }

    @Override
    public Fragment getPairingHelper() {
        return null;
    }

    @Override
    public MeasuringProtocol getMeasuringProtocol(int measurementKind) {
        switch (measurementKind) {
            case Measurement.TYPE_BREATHING_RATE:
                return new HexoSkinRespirationProtocol(this.getConnection(), getSampleDispatcher(), this);
            case Measurement.TYPE_HEART_RATE:
                return new HexoSkinHeartRateProtocol(this.getConnection(), getSampleDispatcher(), this);
            case Measurement.TYPE_STEPS_SNAPSHOT:
                return new HexoSkinAccelerometerProtocol(this.getConnection(), getSampleDispatcher(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "HexoSkin";
    }
}