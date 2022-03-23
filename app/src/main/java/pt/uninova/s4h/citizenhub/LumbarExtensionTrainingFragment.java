package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LumbarExtensionTrainingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lumbar_extension_training, container, false);
        final Button searchButton = view.findViewById(R.id.medex_fragment_search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(LumbarExtensionTrainingFragment.this.requireView()).navigate(LumbarExtensionTrainingFragmentDirections.actionLumbarExtensionTrainingFragmentToLumbarExtensionTrainingSearchFragment());

            }
        });

        return view;
    }

}
