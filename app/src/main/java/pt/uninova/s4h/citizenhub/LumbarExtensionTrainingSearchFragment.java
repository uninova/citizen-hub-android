package pt.uninova.s4h.citizenhub;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pt.uninova.s4h.citizenhub.connectivity.Device;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnection;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothConnectionState;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.BluetoothScanner;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx.MedXAgent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.medx.MedXTrainingProtocol;
import pt.uninova.s4h.citizenhub.data.MedXTrainingValue;
import pt.uninova.s4h.citizenhub.data.Sample;
import pt.uninova.s4h.citizenhub.persistence.ConnectionKind;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRecord;
import pt.uninova.s4h.citizenhub.persistence.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.util.messaging.Observer;

public class LumbarExtensionTrainingSearchFragment extends BluetoothFragment {

    private DeviceListAdapter adapter;

    private BluetoothScanner scanner;

    private void buildRecycleView(View view) throws SecurityException {
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView_searchList);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        final LumbarExtensionTrainingRepository lumbarExtensionTrainingRepository = new LumbarExtensionTrainingRepository(requireActivity().getApplication());

        adapter = new DeviceListAdapter(item -> {
            final Device device = item.getDevice();
            final ProgressBar progressBar = requireView().findViewById(R.id.progressBar);

            progressBar.setVisibility(View.VISIBLE);

            if (scanner != null) {
                scanner.stop();
            }

            final BluetoothConnection bluetoothConnection = new BluetoothConnection();
            final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            final BluetoothDevice bluetoothDevice = adapter.getRemoteDevice(device.getAddress());

            bluetoothConnection.addConnectionStateChangeListener(state -> {
                if (state.getOldState() == BluetoothConnectionState.CONNECTED && state.getNewState() == BluetoothConnectionState.READY) {
                    final MedXAgent agent = new MedXAgent(bluetoothConnection);

                    agent.addSampleObserver(new Observer<Sample>() {
                        @Override
                        public void observe(Sample sample) {
                            agent.disable();
                            agent.removeSampleObserver(this);
                            bluetoothConnection.close();

                            final MedXTrainingValue val = (MedXTrainingValue) sample.getMeasurements()[0].getValue();
                            int duration = (int) val.getDuration().toMillis() / 1000;

                            lumbarExtensionTrainingRepository.create(new LumbarExtensionTrainingRecord(sample.getTimestamp(), duration, val.getScore(), val.getRepetitions(), val.getWeight(), val.getCalories()));

                            navigateToSummaryFragment();
                        }
                    });

                    agent.enable();
                    agent.enableMeasurement(MeasurementKind.LUMBAR_EXTENSION_TRAINING);
                }
            });

            bluetoothDevice.connectGatt(requireContext(), false, bluetoothConnection, BluetoothDevice.TRANSPORT_LE);
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    protected void onBluetoothAllowed() {
        scanner = new BluetoothScanner((BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE));

        scanner.start((address, name) -> {
            final Device device = new Device(address, name == null ? address : name, ConnectionKind.BLUETOOTH);

            adapter.addItem(new DeviceListItem(device, R.drawable.ic_devices_unpaired));
        }, MedXTrainingProtocol.UUID_SERVICE);
    }

    @Override
    protected void onBluetoothDenied() {

    }

    @Override
    protected void onBluetoothDisabled() {

    }

    @Override
    protected void onBluetoothUnsupported() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_search, container, false);

        buildRecycleView(view);

        return view;
    }

    @Override
    protected void onLocationDisabled() {

    }

    @Override
    protected void onLocationUnsupported() {

    }

    @Override
    public void onStop() {
        super.onStop();

        if (scanner != null) {
            scanner.stop();
            scanner = null;
        }
    }

    private void navigateToSummaryFragment() {
        requireActivity().runOnUiThread(() -> Navigation.findNavController(requireView()).navigate(LumbarExtensionTrainingSearchFragmentDirections.actionLumbarExtensionTrainingSearchFragmentToSummaryFragment()));
    }
}
