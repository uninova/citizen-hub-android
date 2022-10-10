package pt.uninova.s4h.citizenhub;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pt.uninova.s4h.citizenhub.service.CitizenHubService;

public class ServiceFragment extends Fragment {

    private CitizenHubService service;
    private ServiceConnection serviceConnection;

    public CitizenHubService getService() {
        return service;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                service = ((CitizenHubService.Binder) iBinder).getService();
                ServiceFragment.this.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                service = null;
                ServiceFragment.this.onServiceDisconnected();
            }
        };

        CitizenHubService.bind(requireContext(), serviceConnection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (serviceConnection != null)
            requireContext().unbindService(serviceConnection);
    }

    public void onServiceConnected() {
    }

    public void onServiceDisconnected() {
    }
}
