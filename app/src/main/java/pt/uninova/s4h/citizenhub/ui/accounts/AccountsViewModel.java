package pt.uninova.s4h.citizenhub.ui.accounts;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import care.data4life.sdk.Data4LifeClient;

public class AccountsViewModel extends AndroidViewModel {

    private final SharedPreferences preferences;

    public AccountsViewModel(@NonNull Application application) {
        super(application);

        preferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
    }

    public void disableSmartBearAccount() {
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("accounts.smartbear.id");
        editor.putBoolean("accounts.smartbear.enabled", false);

        editor.apply();
    }

    public void enableSmartBearAccount(int id) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("accounts.smartbear.enabled", true);
        editor.putInt("accounts.smartbear.id", id);

        editor.apply();

    }

    public int getSmartBearId() {
        return preferences.getInt("accounts.smartbear.id", -1);
    }

    public boolean hasSmart4HealthAccount() {
        return preferences.getBoolean("accounts.smart4health.enabled", true);
    }

    public boolean hasSmartBearAccount() {
        return preferences.getBoolean("accounts.smartbear.enabled", false);
    }

    public void setSmartBearId(int value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("accounts.smartbear.id", value);

        editor.apply();
    }

    public void disableSmart4HealthAccount() {
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("accounts.smart4health.id");
        editor.putBoolean("accounts.smart4health.enabled", false);
        editor.apply();
    }
}