package datastorage;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static datastorage.SourceContract.CONTENT_AUTHORITY;
import static datastorage.SourceContract.PATH_SOURCE;

/**
 * {@link ContentProvider} for Pets app.
 */
public class SourceProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = SourceProvider.class.getSimpleName();
    private static final int SOURCE = 100;
    private static final int SOURCE_ID = 101;
    private static final String PARAM = " ? ";
    private static final UriMatcher sUriMatcher = builderUriMatcher();
    private static final String EQUAL_TO = " = ";
    private static final String AND = " AND ";
    private SourceDbHelper sourceDbHelper;
    private Context mContext;

    // Static initializer. This is run the first time anything is called from this class.
    private static UriMatcher builderUriMatcher() {            // The calls to addURI() go here, for all of the content URI patterns that the provider
        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_SOURCE, SOURCE);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_SOURCE + "/#", SOURCE_ID);
        return sUriMatcher;
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        sourceDbHelper = new SourceDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public void openAndQueryDatabase(SourceDbHelper sourceDbHelper) {
        try {
            SQLiteDatabase newDB = sourceDbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT name, address,connected FROM " +
                    SourceContract.SourceEntry.TABLE_NAME +
                    " where connected > 0", null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String name = c.getString(c.getColumnIndex("name"));
                        String address = c.getString(c.getColumnIndex("address"));
                        Log.d("ONCREATEE", name + "," + address);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
    }
}


