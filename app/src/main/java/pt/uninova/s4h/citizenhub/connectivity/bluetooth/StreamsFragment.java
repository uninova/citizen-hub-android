package pt.uninova.s4h.citizenhub.connectivity.bluetooth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class StreamsFragment extends AbstractConfigurationFragment {
    private final Agent agent;
    private ListView streamsListView;

    private final Observer<StateChangedMessage<Integer, ? extends Agent>> agentStateObserver = value -> {
        streamsListView.deferNotifyDataSetChanged();
        requireActivity().runOnUiThread(() -> loadSupportedFeatures(streamsListView));
    };

    public StreamsFragment(Agent agent) {
        super(agent);
        this.agent = agent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_device_streams_listview, container, false);
        streamsListView = view.findViewById(R.id.streamsListView);
        loadSupportedFeatures(streamsListView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (agent != null) {
            agent.addStateObserver(agentStateObserver);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (agent != null) {
            agent.removeStateObserver(agentStateObserver);
        }
    }

}
