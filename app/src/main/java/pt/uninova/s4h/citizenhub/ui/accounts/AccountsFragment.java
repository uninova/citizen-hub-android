package pt.uninova.s4h.citizenhub.ui.accounts;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.uninova.s4h.citizenhub.R;

public class AccountsFragment extends Fragment {

    private AccountsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.accounts_fragment, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);

        setHasOptionsMenu(true);

        view.findViewById(R.id.accounts_fragment_smart4health).setVisibility(viewModel.hasSmart4HealthAccount() ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.accounts_fragment_smartbear).setVisibility(viewModel.hasSmartBearAccount() ? View.VISIBLE : View.GONE);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.accounts_fragment, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

    }
}