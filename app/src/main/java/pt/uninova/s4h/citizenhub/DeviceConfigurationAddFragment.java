package pt.uninova.s4h.citizenhub;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {

    private DeviceViewModel model;
    private TextView featureMessage;
    private ListView labelListView;
    private TextView loadingTextview;

    private ProgressBar progressBar;
    private ProgressDialog dialog;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.fragment_device_configuration_add_calibration_completed_toast), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_add, container, false);
        connectDevice = view.findViewById(R.id.buttonConfiguration);
        featureMessage = view.findViewById(R.id.textConfigurationMeasurements);
        labelListView = view.findViewById(R.id.listViewLabel);
        loadingTextview = view.findViewById(R.id.device_configuration_loading_textview);
        progressBar = view.findViewById(R.id.add_pprogressBar);
        labelListView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        connectDevice.setVisibility(View.INVISIBLE);
        loadingTextview.setVisibility(View.VISIBLE);
        featureMessage.setVisibility(View.INVISIBLE);

        setupViews(view);
        setupText();

        model.identifySelectedDevice(agent -> {

            if (agent == null) {
                DeviceConfigurationAddFragment.this.requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Navigation.findNavController(DeviceConfigurationAddFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceUnsupportedFragment());
                    }
                });
            }

            DeviceConfigurationAddFragment.this.requireActivity().runOnUiThread(DeviceConfigurationAddFragment.this::loadSupportedFeatures);


            connectDevice.setOnClickListener(v -> {

                model.addAgent(agent);

                DeviceConfigurationAddFragment.this.saveFeaturesChosen();

                Navigation.findNavController(DeviceConfigurationAddFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceConfigurationUpdateFragment());
            });

            DeviceConfigurationAddFragment.this.requireActivity().runOnUiThread(() -> {
                assert agent != null;
                labelListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item_label, getLabelList(agent)));
                labelListView.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                featureMessage.setVisibility(View.VISIBLE);
                loadingTextview.setVisibility(View.GONE);
                connectDevice.setText(R.string.fragment_device_configuration_connect_option_text);
                connectDevice.setVisibility(View.VISIBLE);
            });


        });

        return view;
    }


}

