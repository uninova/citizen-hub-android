package pt.uninova.s4h.citizenhub.connectivity.bluetooth.digitsole;

import android.content.Context;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import pt.uninova.s4h.citizenhub.connectivity.AgentOrchestrator;
import pt.uninova.s4h.citizenhub.connectivity.MeasuringProtocol;
import pt.uninova.s4h.citizenhub.connectivity.RoomSettingsManager;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AdvancedConfigurationMenuItem;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.data.Measurement;

public class DigitsoleAgent extends BluetoothAgent {

    public static final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.digitsole");

    public static final Set<Integer> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            Measurement.TYPE_STEPS_SNAPSHOT
    )));

    private static final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            DigitsoleActivityProtocol.ID
    )));


    public DigitsoleAgent(BluetoothConnection connection, Context context) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection, new RoomSettingsManager(context, connection.getAddress()));
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(int kind) {
        if (kind == Measurement.TYPE_STEPS_SNAPSHOT) {
            return new DigitsoleActivityProtocol(getConnection(), getSampleDispatcher(), this);
        }
        return null;
    }

    @Override
    public String getName() {
        return "Digitsole";
    }

    @Override
    public List<Fragment> getConfigurationFragments() {
        return null;
    }

    @Override
    public List<AdvancedConfigurationMenuItem> getConfigurationMenuItems() {
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
}