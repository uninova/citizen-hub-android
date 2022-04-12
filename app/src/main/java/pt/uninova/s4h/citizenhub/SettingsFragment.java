package pt.uninova.s4h.citizenhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.uninova.util.WorkTimeRangeConverter;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String KEY_EDIT_TEXT_PREFERENCE = "workdays";
    private static final String KEY_WORK_TIME_START = "workStart";
    private static final String KEY_WORK_TIME_END = "workEnd";
    public static List<String> days;
    public static String workStart = "09:00";
    public static String workEnd = "17:00";
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        days = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity().getApplicationContext());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        PreferenceManager.getDefaultSharedPreferences(requireContext());
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        updatePreference(KEY_EDIT_TEXT_PREFERENCE);
    }


    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            dialogFragment = new TimePreferenceDialogFragmentCompat();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);

        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(key);
    }

    private void refreshPreferences(Context context) {
        WorkTimeRangeConverter workTimeRangeConverter = WorkTimeRangeConverter.getInstance(context);
        workTimeRangeConverter.refreshTimeVariables(context, days);
    }

    private void updatePreference(String key) {
        if (key.equals(KEY_EDIT_TEXT_PREFERENCE)) {
            Preference preference = findPreference(key);
            if (preference instanceof MultiSelectListPreference) {
                MultiSelectListPreference pref = (MultiSelectListPreference) preference;
                if (getCurrentEntries(pref).size() > 0) {
                    days = (getCurrentEntries(pref));
                    preferences.edit().putStringSet("weekdays", new HashSet<>(days)).apply();

                    pref.setSummary(getString(R.string.fragment_settings_current_work_days_text) + getCurrentEntries(pref));
                } else {
                    pref.setSummary(getString(R.string.fragment_settings_choose_work_days_text));
                }
            }
            preferences.edit().putStringSet("workDays", new HashSet<>(days)).apply();
        }
        if (key.equals(KEY_WORK_TIME_START)) {
            workStart = preferences.getString(KEY_WORK_TIME_START, "09:00");
        }
        if (key.equals(KEY_WORK_TIME_END)) {
            workEnd = preferences.getString(KEY_WORK_TIME_END, "17:00");
        }
        refreshPreferences(requireContext());
    }

    public List<String> getCurrentEntries(MultiSelectListPreference preference) {
        CharSequence[] entries = preference.getEntries();
        CharSequence[] entryValues = preference.getEntryValues();
        List<String> currentEntries = new ArrayList<>();
        Set<String> currentEntryValues = preference.getValues();

        for (int i = 0; i < entries.length; i++)
            if (currentEntryValues.contains(entryValues[i]))
                currentEntries.add(entries[i].toString());

        return currentEntries;
    }
}
