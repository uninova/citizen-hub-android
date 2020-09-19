package pt.uninova.s4h.citizenhub;

import android.app.Application;
import care.data4life.sdk.Data4LifeClient;

public class CitizenHubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Data4LifeClient.init(this);
    }
}
