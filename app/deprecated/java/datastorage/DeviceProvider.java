package datastorage;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import pt.uninova.s4h.citizenhub.ui.Home;

import static datastorage.DeviceContract.CONTENT_AUTHORITY;
import static datastorage.DeviceContract.DeviceEntry;
import static datastorage.DeviceContract.DeviceEntry.TABLE_NAME;
import static datastorage.DeviceContract.PATH_DEVICES;

/**
 * {@link ContentProvider} for Pets app.
 */
public class DeviceProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = DeviceProvider.class.getSimpleName();
    private static final int DEVICES = 100;
    private static final int DEVICES_ID = 101;
    private static final String PARAM = " ? ";
    private static final UriMatcher sUriMatcher = builderUriMatcher();
    private static final String EQUAL_TO = " = ";
    private static final String AND = " AND ";
    private DeviceDbHelper deviceDbHelper;
    private Context mContext;

    // Static initializer. This is run the first time anything is called from this class.
    private static UriMatcher builderUriMatcher() {            // The calls to addURI() go here, for all of the content URI patterns that the provider
        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_DEVICES, DEVICES);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_DEVICES + "/#", DEVICES_ID);
        return sUriMatcher;
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        deviceDbHelper = new DeviceDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = deviceDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                cursor = database.query(TABLE_NAME, projection, DeviceEntry.COLUMN_DEVICE_ADDRESS, selectionArgs, null, null, sortOrder);
                break;
            case DEVICES_ID:
                selection = DeviceEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }
        Home.homecontext.getContentResolver().notifyChange(uri, null);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) throws UnsupportedOperationException, SQLException {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                return insertDevice(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertDevice(Uri uri, ContentValues values) {
        String devicename = values.getAsString(DeviceEntry.COLUMN_DEVICE_NAME);

        if (devicename == null) {
            throw new IllegalArgumentException("Agent requires a name");
        }

        String deviceaddress = values.getAsString(DeviceEntry.COLUMN_DEVICE_ADDRESS);

        if (deviceaddress == null) {
            throw new IllegalArgumentException("Agent requires an adress");
        }


        SQLiteDatabase db = Home.deviceDbHelper.getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        Home.homecontext.getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                return updateDevice(uri, contentValues, selection, selectionArgs);
            case DEVICES_ID:
                // Para o código PET_ID, extraia o ID do URI,
                // para que saibamos qual registro atualizar. Selection será "_id=?" and selection
                // args será um String array contendo o atual ID.
                selection = DeviceEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateDevice(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateDevice(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_NAME)) {
            String devicename = values.getAsString(DeviceEntry.COLUMN_DEVICE_NAME);

            if (devicename == null) {
                throw new IllegalArgumentException("devicename requires a name");
            }
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_ADDRESS)) {
            String deviceaddress = values.getAsString(DeviceEntry.COLUMN_DEVICE_ADDRESS);

            if (deviceaddress == null) {
                throw new IllegalArgumentException("devicename requires an address");
            }
        }


        //TODO BLOBS FOR SERVICES AND CHARACTERISTICS

        SQLiteDatabase database = deviceDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = deviceDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case DEVICES_ID:
                selection = DeviceEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                return DeviceEntry.CONTENT_LIST_TYPE;
            case DEVICES_ID:
                return DeviceEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    public void openAndQueryDatabase(DeviceDbHelper deviceDbHelper) {
        try {
            SQLiteDatabase newDB = deviceDbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT name, address,connected FROM " +
                    TABLE_NAME +
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

