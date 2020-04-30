package pt.uninova.s4h.citizenhub.ui;

import android.os.Binder;

public class ServiceBinder extends Binder {

    private BackgroundServiceInterface backgroundServiceInterface = new BackgroundService();

    public ServiceBinder(Home home) {
        this.backgroundServiceInterface = backgroundServiceInterface;
    }

    BackgroundServiceInterface getService() {
        return backgroundServiceInterface;
    }
}
