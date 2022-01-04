package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class SettingsFragment extends Fragment {

    private SettingsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_fragment, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

        setHasOptionsMenu(true);


        return view;
    }

}
