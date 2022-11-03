package pt.uninova.s4h.citizenhub.connectivity.bluetooth.kbzposture;

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

public class KbzPostureAgent extends BluetoothAgent {

    static public final UUID ID = AgentOrchestrator.namespaceGenerator().getUUID("bluetooth.kbzposture");

    static private final Set<Integer> supportedMeasurementKinds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            Measurement.TYPE_POSTURE
    )));

    static private final Set<UUID> supportedProtocolsIds = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList(
            KbzBodyProtocol.ID
    )));

    public KbzPostureAgent(BluetoothConnection connection, Context context) {
        super(ID, supportedProtocolsIds, supportedMeasurementKinds, connection, new RoomSettingsManager(context, connection.getAddress()));
    }

    @Override
    protected MeasuringProtocol getMeasuringProtocol(int kind) {
        if (kind == Measurement.TYPE_POSTURE) {
            return new KbzBodyProtocol(this.getConnection(), getSampleDispatcher(), this);
        }

        return null;
    }

    @Override
    public String getName() {
        return "KBZ Posture";
    }

    @Override
    public List<Fragment> getConfigurationFragments() {
        return null;
    }

    @Override
    public Fragment getPairingHelper() {
        return null;
    }
}
