package pt.uninova.s4h.citizenhub.connectivity;

import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public interface SettingsManager {

    void get(String name, Observer<String> observer);

    void set(String name, String value);
}
