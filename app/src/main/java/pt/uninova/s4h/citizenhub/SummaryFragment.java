package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import pt.uninova.s4h.citizenhub.persistence.DailySummary;
import pt.uninova.s4h.citizenhub.persistence.DailySummaryViewModel;

public class SummaryFragment extends Fragment {

    private DailySummaryViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DailySummaryViewModel.class);

        // TODO: match with proper layout
        TextView viewTextHomeSitting = getView().findViewById(R.id.textHome_sitting);

        model.getDailySummary().observe(getViewLifecycleOwner(), new Observer<DailySummary>() {
            @Override
            public void onChanged(DailySummary dailySummary) {
                // TODO: match with proper layout
                if (dailySummary != null)
                    viewTextHomeSitting.setText(" " + dailySummary.getAverageHeartRate());
            }
        });
    }
}