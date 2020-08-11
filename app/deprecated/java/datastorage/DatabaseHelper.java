package datastorage;

import android.database.sqlite.SQLiteDatabase;

interface DatabaseHelper extends AutoCloseable {
    String LOG_TAG = DatabaseHelper.class.getSimpleName();

    void onCreate(SQLiteDatabase db);

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
