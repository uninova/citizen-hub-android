package pt.uninova.s4h.citizenhub.service;

import android.os.Binder;

public class WearOSServiceBinder extends Binder {

    private final WearOSMessageService service;

    public WearOSServiceBinder(WearOSMessageService service) {
        this.service = service;
    }

    public WearOSMessageService getService() {
        return service;
    }

}
