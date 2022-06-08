package pt.uninova.s4h.citizenhub.ui.location;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pt.uninova.s4h.citizenhub.DeviceSearchFragment;
import pt.uninova.s4h.citizenhub.DeviceSearchFragmentDirections;
import pt.uninova.s4h.citizenhub.R;

public class LocationDisabledFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_location_disabled, container, false);

        Button openLocationSettings = result.findViewById(R.id.fragment_location_disabled_button);
        openLocationSettings.setOnClickListener(
                v -> {
                    final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });


        return result;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLocationEnabled(getContext())){
            Navigation.findNavController(requireView()).navigate(LocationDisabledFragmentDirections.actionLocationDisabledFragmentToDeviceSearchFragment());
        }

    }

    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This was deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }
}
