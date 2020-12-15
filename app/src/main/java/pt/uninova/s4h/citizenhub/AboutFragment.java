package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_about, container, false);
        TextView clickableTextLink = result.findViewById(R.id.text_about_clickable_website);
        String linkText = getString(R.string.about_website_smart4health);
        clickableTextLink.setText(Html.fromHtml(linkText));
        clickableTextLink.setMovementMethod(LinkMovementMethod.getInstance());
        Button rpgdButton = (Button) result.findViewById(R.id.about_rpgd_button);
        rpgdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToRpgdFragment());
            }
        });
        Button licenseOfUseButton = (Button) result.findViewById(R.id.about_licenses_button);
        licenseOfUseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToLicenseOfUseFragment());
            }
        });

        /*
        //to test loading screen
        final View result = inflater.inflate(R.layout.fragment_loading, container, false);
        ProgressDialog nDialog;
        nDialog = new ProgressDialog(getContext());
        nDialog.setMessage("Loading Device Data..");
        nDialog.setTitle("Retrieving Data"); //nice without this
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
        nDialog.dismiss(); //TO remove dialog (still shows progress stuff)*/

        return result;
    }
}