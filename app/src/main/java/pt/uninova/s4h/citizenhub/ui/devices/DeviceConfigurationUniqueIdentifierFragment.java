package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.bluetooth.AbstractConfigurationFragment;

public class DeviceConfigurationUniqueIdentifierFragment extends AbstractConfigurationFragment {

    private final Agent agent;
    private LinearLayout udiButton;
    TextView udiTextView;

    public DeviceConfigurationUniqueIdentifierFragment(Agent agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_configuration_unique_identifier_item, container, false);
        udiButton = view.findViewById(R.id.unique_device_identifier_layout);
        udiTextView = view.findViewById(R.id.udiTextViewValue);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        agent.getSettingsManager().get("uid", value -> {
            if (value != null && !value.equals("")) {
                udiTextView.setText(value);
            }
        });

        udiButton.setOnClickListener(view1 -> Navigation.findNavController(DeviceConfigurationUniqueIdentifierFragment.this.requireView()).navigate(DeviceConfigurationFragmentDirections.actionDeviceConfigurationFragmentToDeviceConfigurationUdiSetterFragment()));
    }
}
