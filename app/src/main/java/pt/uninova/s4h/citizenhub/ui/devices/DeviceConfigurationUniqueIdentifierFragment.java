package pt.uninova.s4h.citizenhub.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;

public class DeviceConfigurationUniqueIdentifierFragment extends Fragment {

    private LinearLayout udiButton;
    private DeviceViewModel model;
    TextView udiTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
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

        model.getSelectedDeviceAgent().getSettingsManager().get("uid", value -> {
            if (value != null && !value.equals("")) {
                udiTextView.setText(value);
            }
        });

        udiButton.setOnClickListener(view1 -> Navigation.findNavController(DeviceConfigurationUniqueIdentifierFragment.this.requireView()).navigate(DeviceConfigurationFragmentDirections.actionDeviceConfigurationFragmentToDeviceConfigurationUdiSetterFragment()));
    }
}
