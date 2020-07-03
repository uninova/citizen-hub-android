package pt.uninova.s4h.citizenhub.datastorage;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private static DatabaseClient dbInstance;
    private Context context;
    private DatabaseManager dbManager;

    private DatabaseClient(Context context, String databaseName) {
        this.context = context;

        dbManager = Room.databaseBuilder(context, DatabaseManager.class, databaseName).build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DatabaseClient(context, "citizenhub.db");
        }
        return dbInstance;
    }

    public DatabaseManager getAppDatabase() {
        return dbManager;
    }
}
