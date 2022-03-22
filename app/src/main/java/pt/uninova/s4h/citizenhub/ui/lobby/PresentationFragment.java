package pt.uninova.s4h.citizenhub.ui.lobby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.File;
import java.io.IOException;

import pt.uninova.s4h.citizenhub.R;

public class PresentationFragment extends Fragment {
    private Button skipButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_presentation, container, false);
        skipButton = result.findViewById(R.id.presentation_fragment_skip_button);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEntryFile();
                Navigation.findNavController(requireView()).navigate(PresentationFragmentDirections.actionPresentationFragmentToAuthenticationFragment());
            }
        });

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!firstInteraction()) {
            Navigation.findNavController(requireView()).navigate(PresentationFragmentDirections.actionPresentationFragmentToAuthenticationFragment());
        }
        else{
            createEntryFile();
            final Activity activity = requireActivity();
            final Intent intent = new Intent(activity, SlideActivity.class);
//                    Navigation.findNavController(getView()).navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToSummaryFragment());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            activity.startActivity(intent);
            activity.finish();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public boolean firstInteraction() {
        final File file = new File(requireContext().getFilesDir(), "first.txt");
        return !file.exists();
    }

    public void createEntryFile(){
        final File file = new File(requireContext().getFilesDir(), "first.txt");

        boolean fileCreated = false;

        try {
            fileCreated = file.createNewFile();
        } catch (IOException ioe) {
            System.out.println("Error while creating empty file: " + ioe);
        }

        if (fileCreated) {
            System.out.println("Created empty file: " + file.getPath());
        } else {
            System.out.println("Failed to create empty file: " + file.getPath());
        }
    }

}
