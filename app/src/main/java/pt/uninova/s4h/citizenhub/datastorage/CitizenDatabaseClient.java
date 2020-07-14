package pt.uninova.s4h.citizenhub.datastorage;

import android.content.Context;

import androidx.room.Room;

public class CitizenDatabaseClient {

    private static CitizenDatabaseClient INSTANCE;
    private CitizenDatabase database;

    public CitizenDatabaseClient(Context context) {

        database = Room.databaseBuilder(context, CitizenDatabase.class, "citizen").build();
    }

    public static synchronized CitizenDatabaseClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CitizenDatabaseClient(context);
        }
        return INSTANCE;
    }

    public CitizenDatabase getDatabase() {
        return database;
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        database.close();
    }

}
