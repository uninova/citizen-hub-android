package pt.uninova.s4h.citizenhub;

import android.app.Application;

import care.data4life.sdk.Data4LifeClient;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;

public class CitizenHubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Data4LifeClient.init(this);
        CitizenHubService.start(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        CitizenHubService.stop(this);
    }
}
