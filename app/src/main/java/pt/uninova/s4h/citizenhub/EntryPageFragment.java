package pt.uninova.s4h.citizenhub;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.File;

public class EntryPageFragment  extends Fragment {
    private final File file = new File(requireContext().getFilesDir(),"first.txt");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onEntry();
    }

    public boolean firstInteraction(){
        return !file.exists();
    }

    public void onEntry(){
        if(firstInteraction())
        {
            Navigation.findNavController(requireView()).navigate(EntryPageFragmentDirections.actionEntryPageFragmentToPresentationFragment());
        }
        else {
            Navigation.findNavController(requireView()).navigate(EntryPageFragmentDirections.actionEntryPageFragmentToSummaryFragment());
        }
    }

}
