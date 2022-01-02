package pt.uninova.s4h.citizenhub.ui.accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.R;

public class AddAccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.accounts_fragment, container, false);

        AccountsViewModel viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);

        View smart4HealthCard = view.findViewById(R.id.card_smart4health);
        View smartBearCard = view.findViewById(R.id.card_smartbear);

        smart4HealthCard.setVisibility(!viewModel.hasSmart4HealthAccount() ? View.VISIBLE : View.GONE);
        smartBearCard.setVisibility(!viewModel.hasSmartBearAccount() ? View.VISIBLE : View.GONE);

        smartBearCard.setOnClickListener((View v) -> {
            Navigation.findNavController(requireView()).navigate(AddAccountFragmentDirections.actionAddAccountFragmentToAdvancedSmartBearAccountGateFragment());
        });

        return view;
    }
}
