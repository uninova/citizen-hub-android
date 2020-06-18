package pt.uninova.s4h.citizenhub.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import static pt.uninova.s4h.citizenhub.storage.DeviceColumns.DeviceEntry.COLUMN_DEVICE_ADDRESS;
import static pt.uninova.s4h.citizenhub.storage.DeviceColumns.DeviceEntry.COLUMN_DEVICE_NAME;
import static pt.uninova.s4h.citizenhub.storage.DeviceColumns.DeviceEntry.COLUMN_DEVICE_STATE;
import static pt.uninova.s4h.citizenhub.storage.DeviceColumns.DeviceEntry.COLUMN_DEVICE_TYPE;
import static pt.uninova.s4h.citizenhub.storage.DeviceColumns.DeviceEntry.TABLE_NAME;


public class DeviceTable {
    public static final String LOG_TAG = DeviceTable.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "devices.db";
    private static final String MEASUREMENTS_DB_NAME = "measurements.db";

    /**
     * DatabaseManager version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 7;
    private final SQLiteDatabase db;

    public DeviceTable(SQLiteDatabase db) {
        this.db = db;
    }

    public static void initialize(SQLiteDatabase db) {
        String SQL_CREATE_DEVICES_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + DeviceColumns.DeviceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_DEVICE_NAME + " TEXT NOT NULL, "
                + COLUMN_DEVICE_ADDRESS + " TEXT NOT NULL UNIQUE, "
                + COLUMN_DEVICE_TYPE + " TEXT NOT NULL, "
                + COLUMN_DEVICE_STATE + " TEXT " + " ) ";

        db.execSQL(SQL_CREATE_DEVICES_TABLE);
    }

    public static void upgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public long addRecord(String name, String address, String type, String state) {

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DEVICE_NAME, name);
        cv.put(COLUMN_DEVICE_ADDRESS, address);
        cv.put(COLUMN_DEVICE_TYPE, type);
        cv.put(COLUMN_DEVICE_STATE, state);
        return db.insert(TABLE_NAME, null, cv);

    }

    public long addRecord(DeviceRecord record) {
        return addRecord(record.getName(),record.getAddress(),record.getType(),record.getState());
    }

    public void removeRecord(String address) {
            parseDeleteQuery(address);
    }

    public void removeRecord(DeviceRecord record) {
        parseDeleteQuery(record.getAddress());
    }

    private void parseDeleteQuery(String address){
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_DEVICE_ADDRESS + "='" + address + "'");
        db.close();
    }
    public List<DeviceRecord> getAllRecords() throws Exception {
        String selectAllQuery = " SELECT address, name, state, type FROM " + TABLE_NAME;
        try (Cursor c = db.rawQuery(selectAllQuery, null)) {
            List<DeviceRecord> list = new LinkedList<>();
            while (c.moveToNext()) {
                list.add(parseCursor(c));
            }
            return list;
        }
    }

    private DeviceRecord parseCursor(Cursor c) {
            DeviceRecord r = new DeviceRecord(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4));
            return r;
    }

    public DeviceRecord getRecord(String address) throws Exception {
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where address =?", new String[]{address});
        if (c.moveToFirst()) {

            return parseCursor(c);
        }
        throw new Exception();
    }
    public long setRecord(String name, String address, String type, String state) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEVICE_NAME, name);
        values.put(COLUMN_DEVICE_ADDRESS, address);
        values.put(COLUMN_DEVICE_STATE, state);
        values.put(COLUMN_DEVICE_TYPE, type);
       return db.update(TABLE_NAME, values, "address=?", new String[]{address});
    }

    public long setRecord(DeviceRecord record) {
        return setRecord(record.getName(),record.getAddress(),record.getType(),record.getState());
    }

}