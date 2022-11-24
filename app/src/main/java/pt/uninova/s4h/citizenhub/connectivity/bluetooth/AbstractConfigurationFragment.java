package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pt.uninova.s4h.citizenhub.connectivity.Agent;

public class AbstractConfigurationFragment extends Fragment {

    private Agent agent;

    public AbstractConfigurationFragment(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void enableView(View view, boolean state) {
        view.setEnabled(state);
    }

}
