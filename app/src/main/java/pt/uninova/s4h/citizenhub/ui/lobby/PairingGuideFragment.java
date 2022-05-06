package pt.uninova.s4h.citizenhub.ui.lobby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import pt.uninova.s4h.citizenhub.R;

public class PairingGuideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pairing_guide, container, false);

        final Button skipButton = view.findViewById(R.id.fragment_pairing_guide_button_skip);

        final Button pairDeviceButton = view.findViewById(R.id.fragment_authentication_button_to_search_devices);

        skipButton.setOnClickListener(v -> {
       //     Navigation.findNavController(requireView()).navigate(PairingGuideFragmentDirections.actionPairingGuideFragmentToSummaryFragment());
        });

        pairDeviceButton.setOnClickListener(v -> {
            // Navigation.findNavController(requireView()).navigate(PairingGuideFragmentDirections.actionPairingGuideFragmentToDeviceListFragment());
        });
        return view;
    }
}
