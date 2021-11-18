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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MedExFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_medex, container, false);

        AlertDialog.Builder medExDialogBuilder = new AlertDialog.Builder(getContext());
        medExDialogBuilder.setTitle("MedEx");

        View medexDialogView = getLayoutInflater().inflate(R.layout.dialog_dont_show_message_again, null);
        CheckBox medexCheckBox = medexDialogView.findViewById(R.id.checkBox);

        TextView text = (TextView)medexDialogView.findViewById(R.id.dialog_text);
        text.setMovementMethod(LinkMovementMethod.getInstance());

//        medExDialogBuilder.setMessage(R.string.medex_fragment_dialog_textview);
        medExDialogBuilder.setView(medexDialogView);

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
