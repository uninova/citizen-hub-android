package pt.uninova.s4h.citizenhub;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LumbarExtensionTrainingFragment extends Fragment {
    private Button searchButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lumbar_extension_training, container, false);
        searchButton = view.findViewById(R.id.medex_fragment_search_button);

        AlertDialog.Builder medExDialogBuilder = new AlertDialog.Builder(getContext());
        medExDialogBuilder.setTitle("Lumbar Extension Training");

        View medexDialogView = getLayoutInflater().inflate(R.layout.dialog_dont_show_message_again, null);
        CheckBox medexCheckBox = medexDialogView.findViewById(R.id.checkBox);

        TextView text = medexDialogView.findViewById(R.id.dialog_text);
        text.setMovementMethod(LinkMovementMethod.getInstance());

//        medExDialogBuilder.setMessage(R.string.medex_fragment_dialog_textview);
        medExDialogBuilder.setView(medexDialogView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(LumbarExtensionTrainingFragment.this.requireView()).navigate(LumbarExtensionTrainingFragmentDirections.actionLumbarExtensionTrainingFragmentToLumbarExtensionTrainingSearchFragment());

            }
        });
        medExDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = medExDialogBuilder.create();
        mDialog.show();

        medexCheckBox.setOnCheckedChangeListener((compoundButton, b) -> storeDialogStatus(compoundButton.isChecked()));

        if (getDialogStatus()) {
            mDialog.hide();


        } else {
            mDialog.show();
        }


//        final Button medExButton = view.findViewById(R.id.medex_fragment_button);
//
//        medExButton.setOnClickListener(v -> {
//        }
//        );

        return view;
    }

    private void storeDialogStatus(boolean isChecked) {
        SharedPreferences mSharedPreferences = requireContext().getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus() {
        SharedPreferences mSharedPreferences = requireContext().getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
    }


}
