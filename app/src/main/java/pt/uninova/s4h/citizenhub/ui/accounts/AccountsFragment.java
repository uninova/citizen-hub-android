package pt.uninova.s4h.citizenhub.ui.accounts;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        View smart4HealthCard = view.findViewById(R.id.card_smart4health);
        View smartBearCard = view.findViewById(R.id.card_smartbear);

        smart4HealthCard.setVisibility(viewModel.hasSmart4HealthAccount() ? View.VISIBLE : View.GONE);
        smartBearCard.setVisibility(viewModel.hasSmartBearAccount() ? View.VISIBLE : View.GONE);

        smart4HealthCard.setOnClickListener((View v) -> {
            NavController controller = Navigation.findNavController(requireView());

            controller.navigate(AccountsFragmentDirections.actionAccountsFragmentToSmart4healthAccountFragment());
        });

        smartBearCard.setOnClickListener((View v) -> {
            Navigation.findNavController(requireView()).navigate(AccountsFragmentDirections.actionAccountsFragmentToSmartBearAccountFragment());
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.accounts_fragment, menu);

        MenuItem addItem = menu.findItem(R.id.accounts_fragment_menu_add_item);

        addItem.setOnMenuItemClickListener((MenuItem item) -> {
                    Navigation.findNavController(requireView()).navigate(AccountsFragmentDirections.actionAccountsFragmentToAddAccountFragment());

                    return true;
                }
        );

        if (viewModel.hasSmart4HealthAccount() && viewModel.hasSmartBearAccount()) {
            menu.removeItem(R.id.accounts_fragment_menu_add_item);
        }

    }
}