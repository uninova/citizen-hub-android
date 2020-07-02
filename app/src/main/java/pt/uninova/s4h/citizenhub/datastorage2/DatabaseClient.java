package pt.uninova.s4h.citizenhub.datastorage2;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context context;
    private static DatabaseClient dbInstance;

    //our app database object
    private DatabaseManager dbManager;

    private DatabaseClient(Context context) {
        this.context = context;

        //creating the app database with Room database builder
        // name is the name of the database
        dbManager = Room.databaseBuilder(context, DatabaseManager.class, "citizenhubDatabase").build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DatabaseClient(context);
        }
        return dbInstance;
    }

    public DatabaseManager getAppDatabase() {
        return dbManager;
    }
}
