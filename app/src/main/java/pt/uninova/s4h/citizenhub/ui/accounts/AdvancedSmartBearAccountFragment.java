package pt.uninova.s4h.citizenhub.ui.accounts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Locale;

import pt.uninova.s4h.citizenhub.R;

public class AdvancedSmartBearAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.advanced_smart_bear_account_fragment, container, false);
        final AccountsViewModel viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);
        final EditText patientIdEditText = view.findViewById(R.id.edit_text_patient_id);

        patientIdEditText.setText(String.format(Locale.getDefault(), "%d", viewModel.getSmartBearId()));

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.advanced_smart_bear_account_fragment, menu);

        menu.findItem(R.id.advanced_smart_bear_account_fragment_menu_accept).setOnMenuItemClickListener(item -> {
            final AccountsViewModel viewModel = new ViewModelProvider(requireActivity()).get(AccountsViewModel.class);
            final EditText patientIdEditText = requireView().findViewById(R.id.edit_text_patient_id);

            try {
                int patientId = Integer.parseInt(patientIdEditText.getText().toString());

                viewModel.enableSmartBearAccount(patientId);

                Navigation.findNavController(requireView()).navigate(AdvancedSmartBearAccountFragmentDirections.actionAdvancedSmartBearAccountFragmentToAccountsFragment());
            } catch (NumberFormatException e) {
                patientIdEditText.requestFocus();
                patientIdEditText.setError("Must be an integer");
            }

            return true;
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        View view = requireActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
