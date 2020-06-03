package pt.uninova.s4h.citizenhub.ui;

import android.os.Binder;

public class ServiceBinder extends Binder {

    private BackgroundService backgroundService = new BackgroundService();

    public ServiceBinder(Home home) {
        this.backgroundService = backgroundService;
    }

    BackgroundService getService() {
        return backgroundService;
    }
}
