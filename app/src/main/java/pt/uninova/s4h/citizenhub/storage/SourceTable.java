package pt.uninova.s4h.citizenhub.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import static pt.uninova.s4h.citizenhub.storage.DeviceColumns.DeviceEntry.COLUMN_DEVICE_ADDRESS;
import static pt.uninova.s4h.citizenhub.storage.SourceColumns.SourceEntry.COLUMN_SOURCE_INTERVAL;
import static pt.uninova.s4h.citizenhub.storage.SourceColumns.SourceEntry.COLUMN_SOURCE_TYPE;
import static pt.uninova.s4h.citizenhub.storage.SourceColumns.SourceEntry.COLUMN_SOURCE_UUID;
import static pt.uninova.s4h.citizenhub.storage.SourceColumns.SourceEntry.TABLE_NAME;


public class SourceTable {


    private static final String DATABASE_NAME = "source.db";

    private static final int DATABASE_VERSION = 1;
    private final SQLiteDatabase db;

    public SourceTable(SQLiteDatabase db) {
        this.db = db;
    }

    public static void initialize(SQLiteDatabase db) {
        String SQL_CREATE_SOURCE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + SourceColumns.SourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_SOURCE_UUID + " TEXT NOT NULL, "
                + COLUMN_DEVICE_ADDRESS + " TEXT UNIQUE, "
                + COLUMN_SOURCE_TYPE + " TEXT NOT NULL, "
                + COLUMN_SOURCE_INTERVAL + " INTEGER " + " ) ";

        db.execSQL(SQL_CREATE_SOURCE_TABLE);
    }

    public static void upgrade(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public long addRecord(String uuid, String address, String type, String interval) {

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SOURCE_UUID, uuid);
        cv.put(COLUMN_DEVICE_ADDRESS, address);
        cv.put(COLUMN_SOURCE_TYPE, type);
        cv.put(COLUMN_SOURCE_INTERVAL, interval);
        return db.insert(TABLE_NAME, null, cv);

    }

    public long addRecord(SourceRecord record) {
        return addRecord(record.getUuid(),record.getAddress(),record.getType(),record.getInterval());

    }

    public void removeRecord(String value, String column) {
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + column + "='" + value + "'");
        db.close();
    }

    public void removeRecord(SourceRecord record) {
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_SOURCE_UUID + "='" + record.getUuid() + "'");
        db.close();
    }


    public SourceRecord getRecord(String uuid) throws Exception {
        Cursor c = db.rawQuery("select * from " + TABLE_NAME + " where uuid =?", new String[]{uuid});

        if (c.moveToFirst()) {
            parseCursor(c);
        }

        throw new Exception();
    }

    public List<SourceRecord> getAllRecords() throws Exception {
        String selectAllQuery = " SELECT * FROM " + TABLE_NAME;
        try (Cursor c = db.rawQuery(selectAllQuery, null)) {
            List<SourceRecord> list = new LinkedList<>();
            while (c.moveToNext()) {
                list.add(parseCursor(c));
            }
            return list;
        }
    }

    private SourceRecord parseCursor(Cursor c){
        SourceRecord record = new SourceRecord(
                c.getInt(0),
                c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4));
        return record;
    }

    public long setRecord(String uuid, String address, String type, String interval) {
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SOURCE_UUID, uuid);
        cv.put(COLUMN_DEVICE_ADDRESS, address);
        cv.put(COLUMN_SOURCE_TYPE, type);
        cv.put(COLUMN_SOURCE_INTERVAL, interval);
        return db.update(TABLE_NAME, cv, "uuid=?", new String[]{uuid});

    }

    public long setRecord(SourceRecord record) {
       return setRecord(record.getUuid(),record.getAddress(),record.getType(),record.getInterval());
    }

}