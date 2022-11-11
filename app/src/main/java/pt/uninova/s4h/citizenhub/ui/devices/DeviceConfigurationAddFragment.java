package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;

public class DeviceConfigurationAddFragment extends DeviceConfigurationFragment {

    private DeviceViewModel model;
    private LinearLayout featureMessageLayout;
    private ListView labelListView;
    private TextView loadingTextview;
    private Button connectDevice;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_add, container, false);
        connectDevice = view.findViewById(R.id.buttonConfiguration);
        featureMessageLayout = view.findViewById(R.id.layoutConfigurationMeasurements);
        View divider_view = view.findViewById(R.id.divider_configuration_id);
        divider_view.setAlpha(0.5f);
        labelListView = view.findViewById(R.id.listViewLabel);
        loadingTextview = view.findViewById(R.id.device_configuration_loading_textview);
        progressBar = view.findViewById(R.id.add_pprogressBar);
        labelListView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        connectDevice.setVisibility(View.INVISIBLE);
        loadingTextview.setVisibility(View.VISIBLE);
        featureMessageLayout.setVisibility(View.INVISIBLE);

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
                Navigation.findNavController(DeviceConfigurationAddFragment.this.requireView()).navigate(DeviceConfigurationAddFragmentDirections.actionDeviceConfigurationAddFragmentToDeviceConfigurationUpdateFragment());
            });

            DeviceConfigurationAddFragment.this.requireActivity().runOnUiThread(() -> {
                assert agent != null;
                labelListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.list_item_label, getLabelList(agent)));
                labelListView.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                featureMessageLayout.setVisibility(View.VISIBLE);
                loadingTextview.setVisibility(View.GONE);
                connectDevice.setText(R.string.fragment_device_configuration_connect_option_text);
                connectDevice.setVisibility(View.VISIBLE);
            });


        });

        return view;
    }


}

