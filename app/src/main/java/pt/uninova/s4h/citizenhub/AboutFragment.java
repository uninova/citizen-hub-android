package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class AboutFragment extends Fragment {

    Application app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Application) requireActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_about, container, false);
        TextView clickableTextLink = result.findViewById(R.id.text_about_clickable_contact);
        String linkText = getString(R.string.about_contact_email);
        clickableTextLink.setText(Html.fromHtml(linkText));
        clickableTextLink.setMovementMethod(LinkMovementMethod.getInstance());
        Button rpgdButton = (Button) result.findViewById(R.id.about_privacy_policy_button);
        rpgdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToPrivacyPolicyFragment());
            }
        });
        Button licenseOfUseButton = (Button) result.findViewById(R.id.about_licenses_button);
        licenseOfUseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToLicenseOfUseFragment());
            }
        });

        Button openSourceLicenses = (Button) result.findViewById(R.id.about_open_sources_button);
        openSourceLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToOpenSourceLicensesFragment());
            }
        });

        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}