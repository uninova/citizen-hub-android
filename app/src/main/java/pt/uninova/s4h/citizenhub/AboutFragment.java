package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
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
        app = requireActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.fragment_about, container, false);


        Button privacyPolicyButton = result.findViewById(R.id.about_privacy_policy_button);
        TextView emailTextView = result.findViewById(R.id.text_about_contact);
        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"helpdesk@smart4health.eu"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CitizenHub: Issue Report");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                requireContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        privacyPolicyButton.setOnClickListener(
                v -> Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToPrivacyPolicyFragment()));

        Button licenseOfUseButton = result.findViewById(R.id.about_licenses_button);
        licenseOfUseButton.setOnClickListener(
                v -> Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToLicenseOfUseFragment()));

        Button openSourceLicenses = result.findViewById(R.id.about_open_sources_button);
        openSourceLicenses.setOnClickListener(
                v -> Navigation.findNavController(requireView()).navigate(AboutFragmentDirections.actionAboutFragmentToOpenSourceLicensesFragment()));

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