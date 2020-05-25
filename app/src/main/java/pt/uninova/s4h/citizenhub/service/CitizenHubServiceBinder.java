package pt.uninova.s4h.citizenhub.service;

import android.os.Binder;

public class CitizenHubServiceBinder extends Binder {

    private final CitizenHubService service;

    public CitizenHubServiceBinder(CitizenHubService service) {
        this.service = service;
    }

}
