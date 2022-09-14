package pt.uninova.s4h.citizenhub;

import android.app.AlertDialog;
import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.PreferenceChangeEvent;

import care.data4life.sdk.Data4LifeClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pt.uninova.s4h.citizenhub.service.CitizenHubService;

public class CitizenHubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String bugreport = preferences.getString("bugreport", null);

        if (bugreport != null) {
            Thread t = new Thread(() -> {
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = RequestBody.create(bugreport, MediaType.get("text/plain"));
                Request request = new Request.Builder()
                        .url("https://bugreport.smart4health.grisenergia.pt/stacktrace")
                        .post(requestBody)
                        .build();

                try (final Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        //Toast.makeText(getApplicationContext(), "Bug report sent", Toast.LENGTH_LONG).show();
                        preferences.edit().remove("bugreport").apply();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            t.start();
        }

        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("bugreport", stringWriter.toString());
            editor.apply();

            if (uncaughtExceptionHandler != null)
                uncaughtExceptionHandler.uncaughtException(t, e);
        });

        Data4LifeClient.init(this);
        CitizenHubService.start(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        CitizenHubService.stop(this);
    }
}
