package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

public class SettingsViewModel extends AndroidViewModel {

    private final SharedPreferences preferences;


    public SettingsViewModel(@NonNull Application application) {

        super(application);
        preferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());

    }
}
