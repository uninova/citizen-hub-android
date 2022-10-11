package pt.uninova.s4h.citizenhub;

import static java.util.Objects.requireNonNull;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pt.uninova.s4h.citizenhub.connectivity.Agent;
import pt.uninova.s4h.citizenhub.connectivity.StateChangedMessage;
import pt.uninova.s4h.citizenhub.data.Device;
import pt.uninova.s4h.citizenhub.ui.devices.DeviceViewModel;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DeviceListFragment extends Fragment {

    private DeviceListAdapter adapter;
    private DeviceViewModel model;

    private void buildRecycleView(View view) {
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView_devicesList);
        final RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(requireContext());

        adapter = new DeviceListAdapter(item -> {

            model.selectDevice(item.getDevice());

            Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConfigurationUpdateFragment());
        });

        adapter.setHasStableIds(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(null);

//        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_device_list, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_device_list, container, false);

        model = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        model.getDeviceList().observe(getViewLifecycleOwner(), this::onDeviceListChanged);
        model.getSelectedAgentLiveData().observe(getViewLifecycleOwner(), this::onAgentStateChange);
//        if(model.getSelectedAgentLiveData().getValue()!=null) {
//            model.getSelectedAgentLiveData().getValue().addStateObserver(new Observer<StateChangedMessage<Integer, ? extends Agent>>() {
//                @Override
//                public void observe(StateChangedMessage<Integer, ? extends Agent> value) {
//                    new Handler(Looper.getMainLooper()) {
//                        @Override
//                        public void handleMessage(Message inputMessage) {
//                            updateItemAgentState(model.getSelectedAgentLiveData().getValue(), value.getNewState());
//                        }
//                    };
//                }
//            });
//        }
        Button searchDevices = result.findViewById(R.id.searchButton);

        buildRecycleView(result);

        setHasOptionsMenu(false);

        searchDevices.setOnClickListener(view -> Navigation.findNavController(requireView()).navigate(DeviceListFragmentDirections.actionDeviceListFragmentToDeviceConnectionMethodFragment()));

        return result;
    }

    private void onAgentStateChange(Agent agent) {
        agent.addStateObserver(new Observer<StateChangedMessage<Integer, ? extends Agent>>() {
            @Override
            public void observe(StateChangedMessage<Integer, ? extends Agent> value) {
//                new Handler(Looper.getMainLooper()) {
//                    @Override
//                    public void handleMessage(Message inputMessage) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateItemAgentState(agent, value.getNewState());
                    }
                });
//                    }
//                };
            }
        });
    }

//    private void onAgentStateChange(Agent agent) {
//            model.getSelectedAgentLiveData().getValue().addStateObserver(new Observer<StateChangedMessage<Integer, ? extends Agent>>() {
//                @Override
//                public void observe(StateChangedMessage<Integer, ? extends Agent> value) {
//                    new Handler(Looper.getMainLooper()) {
//                        @Override
//                        public void handleMessage(Message inputMessage) {
//                            updateItemAgentState(model.getSelectedAgentLiveData().getValue(), value.getNewState());
//                        }
//                    };
//                }
//            });
//    }

    @Override
    public void onResume() {
        super.onResume();

        TextView noDevices = requireActivity().findViewById(R.id.fragment_device_list_no_data);

        if (adapter.getItemCount() == 0) {
            noDevices.setVisibility(View.VISIBLE);
        } else {
            noDevices.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateItemAgentState(Agent agent, int state) {
        int pos = requireNonNull(model.getDeviceList().getValue()).indexOf(agent.getSource());
        if (state == 1) {
            adapter.getItem(pos).setImageResource(R.drawable.ic_devices_connected);
        } else {
            adapter.getItem(pos).setImageResource(R.drawable.ic_devices_unpaired);
        }
        adapter.updateItem(pos, adapter.getItem(pos));

    }

    public void onDeviceListChanged(List<Device> deviceList) {
        adapter.clear();

        if (deviceList.size() > 0) {
            for (Device i : deviceList) {
                if (model.getAttachedAgentState(i) == 1) {
                    adapter.addItem(new DeviceListItem(i, R.drawable.ic_devices_connected));
                } else {
                    adapter.addItem(new DeviceListItem(i, R.drawable.ic_devices_unpaired));
                }
            }
        }
    }
}
