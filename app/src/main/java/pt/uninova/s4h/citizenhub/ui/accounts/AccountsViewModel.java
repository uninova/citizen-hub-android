package pt.uninova.s4h.citizenhub.ui.accounts;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

public class AccountsViewModel extends AndroidViewModel {

    private final SharedPreferences preferences;

    public AccountsViewModel(@NonNull Application application) {
        super(application);

        preferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }

    public boolean hasSmart4HealthAccount() {
        return preferences.getBoolean("accounts.smart4health.enabled", true);
    }

    public boolean hasSmartBearAccount() {
        return preferences.getBoolean("accounts.smartbear.enabled", false);
    }

}