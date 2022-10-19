package pt.uninova.s4h.citizenhub.ui.accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.work.WorkManager;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.work.WorkOrchestrator;

public class SmartBearAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.smart_bear_account_fragment, container, false);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.smart_bear_account_fragment, menu);

        menu.findItem(R.id.smart_bear_account_fragment_menu_advanced).setOnMenuItemClickListener(item -> {
            Navigation.findNavController(requireView()).navigate(SmartBearAccountFragmentDirections.actionSmartBearAccountFragmentToAdvancedSmartBearAccountGateFragment());
            return true;
        });

        menu.findItem(R.id.smart_bear_account_fragment_menu_log_out).setOnMenuItemClickListener(item -> {
            final AccountsViewModel viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);
            viewModel.disableSmartBearAccount();

            WorkOrchestrator workOrchestrator = new WorkOrchestrator(WorkManager.getInstance(requireContext()));
            workOrchestrator.cancelSmartBearUploader();

            Navigation.findNavController(requireView()).navigate(SmartBearAccountFragmentDirections.actionSmartBearAccountFragmentToAccountsFragment());
            return true;
        });
    }
}
